package org.basex.query.gflwor;

import static org.basex.util.Array.*;
import static org.basex.query.QueryText.*;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.basex.io.serial.*;
import org.basex.query.*;
import org.basex.query.expr.*;
import org.basex.query.gflwor.GFLWOR.Eval;
import org.basex.query.item.Item;
import org.basex.query.item.Value;
import org.basex.query.util.*;
import org.basex.query.var.*;
import org.basex.util.*;


/**
 * FLWOR {@code order by}-expression.
 *
 * @author BaseX Team 2005-12, BSD License
 * @author Leo Woerteler
 */
public class OrderBy extends GFLWOR.Clause {
  /** Variables to sort. */
  Var[] vars;
  /** Sort keys. */
  final Key[] keys;
  /** Stable sort flag. */
  final boolean stable;

  /**
   * Constructor.
   * @param vs variables to sort
   * @param ks sort keys
   * @param stbl stable sort
   * @param ii input info
   */
  public OrderBy(final Var[] vs, final Key[] ks, final boolean stbl, final InputInfo ii) {
    super(ii);
    vars = vs;
    keys = ks;
    stable = stbl;
  }

  @Override
  Eval eval(final Eval sub) {
    return new Eval() {
      /** Sorted output tuples. */
      private Value[][] tpls;
      /** Permutation of the values. */
      private int[] perm;
      /** Current position. */
      int pos;
      @Override
      public boolean next(final QueryContext ctx) throws QueryException {
        if(tpls == null) init(ctx);
        if(pos == tpls.length) return false;
        final int p = perm[pos++];
        final Value[] tuple = tpls[p];
        // free the space occupied by the tuple
        tpls[p] = null;
        for(int i = 0; i < vars.length; i++) ctx.set(vars[i], tuple[i], input);
        return true;
      }

      /**
       * Caches and sorts all incoming tuples.
       * @param ctx query context
       * @throws QueryException evaluation exception
       */
      private void init(final QueryContext ctx) throws QueryException {
        // keys are stored at off positions, values ad even ones
        List<Value[]> tuples = new ArrayList<Value[]>();
        while(sub.next(ctx)) {
          final Item[] key = new Item[keys.length];
          for(int i = 0; i < keys.length; i++)
            key[i] = keys[i].expr.item(ctx, keys[i].input);
          tuples.add(key);

          final Value[] vals = new Value[vars.length];
          for(int i = 0; i < vars.length; i++) vals[i] = ctx.get(vars[i]);
          tuples.add(vals);
        }

        final int len = tuples.size() >>> 1;
        final Item[][] ks = new Item[len][];
        perm = new int[len];
        tpls = new Value[len][];
        for(int i = 0; i < len; i++) {
          perm[i] = i;
          tpls[i] = tuples.get((i << 1) | 1);
          ks[i] = (Item[]) tuples.get(i << 1);
        }
        // be nice to the garbage collector
        tuples = null;
        sort(ks, 0, len);
      }

      /**
       * Recursively sorts the specified items.
       * The algorithm is derived from {@link Arrays#sort(int[])}.
       * @param start start position
       * @param len end position
       * @throws QueryException query exception
       */
      private void sort(final Item[][] ks, final int start, final int len)
          throws QueryException {
        if(len < 7) {
          // use insertion sort of small arrays
          for(int i = start; i < len + start; i++)
            for(int j = i; j > start && cmp(ks[perm[j - 1]], ks[perm[j]], -1) > 0; j--)
              swap(perm, j, j - 1);
          return;
        }

        // find a good pivot element
        int mid = start + (len >> 1);
        if(len > 7) {
          int left = start, right = start + len - 1;
          if(len > 40) {
            final int k = len >>> 3;
            left = median(ks, left, left + k, left + (k << 1));
            mid = median(ks, mid - k, mid, mid + k);
            right = median(ks, right - (k << 1), right - k, right);
          }
          mid = median(ks, left, mid, right);
        }

        final Item[] pivot = ks[perm[mid]];

        // partition the values
        int a = start, b = a, c = start + len - 1, d = c;
        while(true) {
          while(b <= c) {
            final int h = cmp(ks[perm[b]], pivot, b - mid);
            if(h > 0) break;
            if(h == 0) swap(perm, a++, b);
            ++b;
          }
          while(c >= b) {
            final int h = cmp(ks[perm[c]], pivot, c - mid);
            if(h < 0) break;
            if(h == 0) swap(perm, c, d--);
            --c;
          }
          if(b > c) break;
          swap(perm, b++, c--);
        }

        // Swap pivot elements back to middle
        int k;
        final int n = start + len;
        k = Math.min(a - start, b - a);
        swap(perm, start, b - k, k);
        k = Math.min(d - c, n - d - 1);
        swap(perm, b, n - k, k);

        // recursively sort non-pivot elements
        if((k = b - a) > 1) sort(ks, start, k);
        if((k = d - c) > 1) sort(ks, n - k, k);
      }

      /**
       * Returns the difference of two entries (part of QuickSort).
       * @param a sort keys of first item
       * @param b sort keys of second item
       * @param d sort keys of second item
       * @return result
       * @throws QueryException query exception
       */
      private int cmp(final Item[] a, final Item[] b, final int d) throws QueryException {
        for(int k = 0; k < keys.length; k++) {
          final Key or = keys[k];
          final Item m = a[k], n = b[k];
          final int c = m == null ? n == null ? 0 : or.least ? -1 : 1 :
            n == null ? or.least ? 1 : -1 : m.diff(or.input, n);
          if(c != 0) return or.desc ? -c : c;
        }

        // optional stable sorting
        return stable ? d : 0;
      }

      /**
       * Returns the index of the median of the three indexed integers.
       * @param ks key array
       * @param a first offset
       * @param b second offset
       * @param c thirst offset
       * @return median
       * @throws QueryException query exception
       */
      private int median(final Item[][] ks, final int a, final int b, final int c)
          throws QueryException {
        final Item[] ka = ks[perm[a]], kb = ks[perm[b]], kc = ks[perm[c]];
        return cmp(ka, kb, a - b) < 0
            ? cmp(kb, kc, b - c) < 0 ? b : cmp(ka, kc, a - c) < 0 ? c : a
            : cmp(kb, kc, b - c) > 0 ? b : cmp(ka, kc, a - c) > 0 ? c : a;
      }
    };
  }

  @Override
  public void plan(final Serializer ser) throws IOException {
    ser.openElement(this, Token.token(STABLE), Token.token(stable));
    for(final Key k : keys) k.plan(ser);
    ser.closeElement();
  }

  @Override
  public String toString() {
    final StringBuilder sb = new StringBuilder(ORDER).append(' ').append(BY);
    for(int i = 0; i < keys.length; i++) sb.append(i == 0 ? " " : SEP).append(keys[i]);
    if(stable) sb.append(' ').append(STABLE);
    return sb.toString();
  }

  @Override
  public boolean uses(final Use u) {
    if(u == Use.VAR) return true;
    for(final Key k : keys) if(k.uses(u)) return true;
    return false;
  }

  @Override
  public OrderBy comp(final QueryContext ctx, final VarScope scp) throws QueryException {
    for(final Key k : keys) k.comp(ctx, scp);
    return this;
  }

  @Override
  public boolean removable(final Var v) {
    for(final Key k : keys) if(!k.removable(v)) return false;
    return true;
  }

  @Override
  public OrderBy remove(final Var v) {
    for(final Key k : keys) k.remove(v);
    return this;
  }

  @Override
  public boolean visitVars(final VarVisitor visitor) {
    return visitor.visitAll(keys);
  }

  @Override
  boolean undeclare(final VarVisitor visitor) {
    return true;
  }

  @Override
  public Var[] vars() {
    return new Var[0];
  }

  @Override
  public boolean declares(final Var v) {
    return false;
  }

  /**
   * Sort key.
   *
   * @author BaseX Team 2005-12, BSD License
   * @author Leo Woerteler
   */
  public static class Key extends Single {
    /** Descending order flag. */
    final boolean desc;
    /** Position of empty sort keys. */
    final boolean least;

    /**
     * Constructor.
     * @param ii input info
     * @param k sort key expression
     * @param dsc descending order
     * @param lst empty least
     */
    public Key(final InputInfo ii, final Expr k, final boolean dsc, final boolean lst) {
      super(ii, k);
      desc = dsc;
      least = lst;
    }

    @Override
    public void plan(final Serializer ser) throws IOException {
      ser.openElement(this, DIR, Token.token(desc ? DESCENDING : ASCENDING),
          Token.token(EMPTYORD), Token.token(least ? LEAST : GREATEST));
      expr.plan(ser);
      ser.closeElement();
    }

    @Override
    public String toString() {
      final StringBuilder sb = new StringBuilder(expr.toString());
      if(desc) sb.append(' ').append(DESCENDING);
      if(least) sb.append(' ').append(EMPTYORD).append(' ').append(LEAST);
      return sb.toString();
    }
  }
}
