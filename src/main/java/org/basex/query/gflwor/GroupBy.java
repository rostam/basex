package org.basex.query.gflwor;

import static org.basex.query.QueryText.*;
import static org.basex.query.util.Err.*;

import java.io.*;
import java.util.*;

import org.basex.io.serial.*;
import org.basex.query.*;
import org.basex.query.expr.*;
import org.basex.query.func.*;
import org.basex.query.gflwor.GFLWOR.Eval;
import org.basex.query.item.*;
import org.basex.query.iter.*;
import org.basex.query.util.*;
import org.basex.query.var.*;
import org.basex.util.*;
import org.basex.util.hash.*;
import org.basex.util.list.*;


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
  Var[][] nongroup;

  /**
   * Constructor.
   * @param specs grouping specs
   * @param ngrp non-grouping variables
   * @param ii input info
   */
  public GroupBy(final Spec[] specs, final Var[][] ngrp, final InputInfo ii) {
    super(ii);
    by = specs;
    nongroup = ngrp;
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
        for(int i = 0; i < nongroup[1].length; i++)
          ctx.set(nongroup[1][i], curr.ngv[i].value(), input);
        return true;
      }

      /**
       * Initializes the groups.
       * @param ctx query context
       * @throws QueryException query exception
       */
      private void init(final QueryContext ctx) throws QueryException {
        final ArrayList<Group> grps = new ArrayList<Group>();
        final IntMap<IntList> hashToPos = new IntMap<IntList>();
        while(sub.next(ctx)) {
          final Item[] key = new Item[by.length];
          int hash = 1;
          for(int i = 0; i < by.length; i++) {
            final Value ki = by[i].value(ctx);
            if(ki.size() > 1) XGRP.thrw(input);
            key[i] = ki.item(ctx, input);
            hash = 31 * hash + (key[i] == null ? 0 : key[i].hash(input));
          }

          IntList poss = hashToPos.get(hash);
          Group grp = null;
          if(poss != null) {
            for(int i = poss.size(); --i >= 0;) {
              final Group g = grps.get(poss.get(i));
              if(eq(key, g.key)) {
                grp = g;
                break;
              }
            }
          } else {
            poss = new IntList();
            hashToPos.add(hash, poss);
          }

          if(grp == null) {
            // new group, add it to the list
            final ItemCache[] ngs = new ItemCache[nongroup[0].length];
            for(int i = 0; i < ngs.length; i++) ngs[i] = new ItemCache();
            grp = new Group(key, ngs);
            poss.add(grps.size());
            grps.add(grp);
          }

          for(int j = 0; j < nongroup[0].length; j++)
            grp.ngv[j].add(ctx.get(nongroup[0][j]));
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
      if(a == null ^ b == null || !(a.comparable(b) && a.eq(input, b))) return false;
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
    for(final Var ng : nongroup[1]) if(!visitor.declared(ng)) return false;
    return true;
  }

  @Override
  boolean undeclare(final VarVisitor visitor) {
    for(int i = nongroup[1].length; --i >= 0;)
      if(!visitor.undeclared(nongroup[1][i])) return false;
    for(int i = by.length; --i >= 0;) if(!visitor.undeclared(by[i].var)) return false;
    return true;
  }

  @Override
  public Var[] vars() {
    final Var[] res = new Var[by.length + nongroup[1].length];
    for(int i = 0; i < by.length; i++) res[i] = by[i].var;
    System.arraycopy(nongroup[1], 0, res, by.length, nongroup[1].length);
    return res;
  }

  @Override
  public boolean declares(final Var v) {
    for(final Spec s : by) if(s.var.is(v)) return true;
    for(final Var ng : nongroup[1]) if(ng.is(v)) return true;
    return false;
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
      return value(ctx).item(ctx, ii);
    }

    @Override
    public Value value(final QueryContext ctx) throws QueryException {
      final Value val = expr.value(ctx);
      if(val.size() > 1) throw Err.XGRP.thrw(input);
      return val.isEmpty() ? val : StandardFunc.atom(val.itemAt(0), input);
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
}
