package org.basex.query.gflwor;

import static org.basex.query.QueryText.*;

import java.io.*;
import java.util.*;

import org.basex.io.serial.*;
import org.basex.query.*;
import org.basex.query.expr.*;
import org.basex.query.func.*;
import org.basex.query.gflwor.GFLWOR.Eval;
import org.basex.query.item.*;
import org.basex.query.item.SeqType.Occ;
import org.basex.query.iter.*;
import org.basex.query.util.*;
import org.basex.query.var.*;
import org.basex.util.*;
import org.basex.util.hash.*;


/**
 * The GFLWOR {@code group by} expression.
 *
 * @author BaseX Team 2005-12, BSD License
 * @author Leo Woerteler
 */
public class GroupBy extends GFLWOR.Clause {
  /** Grouping specs. */
  Spec[] by;
  /** Non-grouping variables. */
  LocalVarRef[] pre;
  /** Non-grouping variables. */
  Var[] post;

  /**
   * Constructor.
   * @param specs grouping specs
   * @param pr references to pre-grouping variables
   * @param pst post-grouping variables
   * @param ii input info
   */
  public GroupBy(final Spec[] specs, final LocalVarRef[] pr, final Var[] pst,
      final InputInfo ii) {
    super(ii, vars(specs, pst));
    by = specs;
    pre = pr;
    post = pst;
  }

  /**
   * Gathers all declared variables.
   * @param specs grouping specs
   * @param vs non-grouping variables
   * @return declared variables
   */
  private static Var[] vars(final Spec[] specs, final Var[] vs) {
    final Var[] res = new Var[specs.length + vs.length];
    for(int i = 0; i < specs.length; i++) res[i] = specs[i].var;
    System.arraycopy(vs, 0, res, specs.length, vs.length);
    return res;
  }

  @Override
  Eval eval(final Eval sub) {
    return new Eval() {
      /** Groups to iterate over. */
      private Group[] groups;
      /** Current position. */
      private int pos;

      @Override
      public boolean next(final QueryContext ctx) throws QueryException {
        if(groups == null) init(ctx);
        if(pos == groups.length) return false;

        final Group curr = groups[pos];
        // be nice to the garbage collector
        groups[pos++] = null;

        for(int i = 0; i < by.length; i++)
          ctx.set(by[i].var, curr.key[i] == null ? Empty.SEQ : curr.key[i], input);
        for(int i = 0; i < post.length; i++) ctx.set(post[i], curr.ngv[i].value(), input);
        return true;
      }

      /**
       * Initializes the groups.
       * @param ctx query context
       * @throws QueryException query exception
       */
      private void init(final QueryContext ctx) throws QueryException {
        final ArrayList<Group> grps = new ArrayList<Group>();
        final IntMap<Group> hashMap = new IntMap<Group>();
        while(sub.next(ctx)) {
          final Item[] key = new Item[by.length];
          int hash = 1;
          for(int i = 0; i < by.length; i++) {
            final Item ki = by[i].item(ctx, input);
            key[i] = ki == null ? null : StandardFunc.atom(ki, input);
            hash = 31 * hash + (key[i] == null ? 0 : key[i].hash(input));
          }

          // find the group for this key
          final Group fst = hashMap.get(hash);
          Group grp = null;
          for(Group g = fst; g != null; g = g.next) {
            if(eq(key, g.key)) {
              grp = g;
              break;
            }
          }

          if(grp == null) {
            // new group, add it to the list
            final ItemCache[] ngs = new ItemCache[pre.length];
            for(int i = 0; i < ngs.length; i++) ngs[i] = new ItemCache();
            grp = new Group(key, ngs);
            grps.add(grp);

            // insert the group into the hash table
            if(fst == null) {
              hashMap.add(hash, grp);
            } else {
              final Group nxt = fst.next;
              fst.next = grp;
              grp.next = nxt;
            }
          }

          for(int j = 0; j < pre.length; j++) grp.ngv[j].add(pre[j].value(ctx));
        }

        // we're finished, copy the array so the list can be garbage-collected
        groups = grps.toArray(new Group[grps.size()]);
      }
    };
  }

  /**
   * Checks two keys for equality.
   * @param as first key
   * @param bs second key
   * @return {@code true} if the compare as equal, {@code false} otherwise
   * @throws QueryException query exception
   */
  final boolean eq(final Item[] as, final Item[] bs) throws QueryException {
    for(int i = 0; i < as.length; i++) {
      final Item a = as[i], b = bs[i];
      if(a == null ^ b == null || a != null && !a.equiv(input, b)) return false;
    }
    return true;
  }

  @Override
  public void plan(final Serializer ser) throws IOException {
    ser.openElement(this);
    for(final Spec spec : by) spec.plan(ser);
    ser.closeElement();
  }

  @Override
  public String toString() {
    final StringBuilder sb = new StringBuilder(GROUP).append(' ').append(BY);
    for(int i = 0; i < by.length; i++) sb.append(i == 0 ? " " : SEP).append(by[i]);
    return sb.toString();
  }

  @Override
  public boolean uses(final Use u) {
    if(u == Use.VAR || u == Use.X30) return true;
    for(final Spec sp : by) if(sp.uses(u)) return true;
    return false;
  }

  @Override
  public GroupBy comp(final QueryContext ctx, final VarScope scp) throws QueryException {
    for(int i = 0; i < by.length; i++) by[i].comp(ctx, scp);
    for(int i = 0; i < pre.length; i++) {
      final Var inv = pre[i].var, outv = post[i];
      final SeqType it = inv.type();
      outv.refineType(SeqType.get(it.type, it.mayBeZero() ? Occ.ZERO_MORE : Occ.ONE_MORE),
          input);
    }
    return this;
  }

  @Override
  public boolean removable(final Var v) {
    for(final Spec b : by) if(!b.removable(v)) return false;
    return true;
  }

  @Override
  public Expr remove(final Var v) {
    for(final Spec b : by) b.remove(v);
    return this;
  }

  @Override
  public boolean visitVars(final VarVisitor visitor) {
    if(!visitor.visitAll(by)) return false;
    for(final LocalVarRef ng : pre) if(!visitor.used(ng)) return false;
    for(final Var ng : post) if(!visitor.declared(ng)) return false;
    return true;
  }

  @Override
  boolean clean(final QueryContext ctx, final BitArray used) {
    final int len = pre.length;
    for(int i = 0; i < post.length; i++) {
      if(!used.get(post[i].id)) {
        pre  = Array.delete(pre, i);
        post = Array.delete(post, i--);
      }
    }
    return pre.length < len;
  }

  /**
   * Grouping spec.
   *
   * @author BaseX Team 2005-12, BSD License
   * @author Leo Woerteler
   */
  public static final class Spec extends Single {
    /** Grouping variable. */
    public final Var var;

    /**
     * Constructor.
     *
     * @param ii input info
     * @param v grouping variable
     * @param e grouping expression
     */
    public Spec(final InputInfo ii, final Var v, final Expr e) {
      super(ii, e);
      var = v;
    }

    @Override
    public void plan(final Serializer ser) throws IOException {
      ser.openElement(this);
      var.plan(ser);
      expr.plan(ser);
      ser.closeElement();
    }

    @Override
    public String toString() {
      return var + " " + ASSIGN + ' ' + expr;
    }

    @Override
    public Item item(final QueryContext ctx, final InputInfo ii) throws QueryException {
      final Value val = expr.value(ctx);
      if(val.size() > 1) throw Err.XGRP.thrw(input);
      return val.isItem() ? (Item) val : null;
    }

    @Override
    public boolean visitVars(final VarVisitor visitor) {
      return expr.visitVars(visitor) && visitor.declared(var);
    }
  }

  /**
   * A group of tuples of post-grouping variables.
   *
   * @author BaseX Team 2005-12, BSD License
   * @author Leo Woerteler
   */
  private static final class Group {
    /** Grouping key, may contain {@code null} values. */
    final Item[] key;
    /** Non-grouping variables. */
    final ItemCache[] ngv;
    /** Overflow list. */
    Group next;

    /**
     * Constructor.
     * @param k grouping key
     * @param ng non-grouping variables
     */
    Group(final Item[] k, final ItemCache[] ng) {
      key = k;
      ngv = ng;
    }
  }

  @Override
  boolean skippable(final Let let) {
    return false;
  }
}
