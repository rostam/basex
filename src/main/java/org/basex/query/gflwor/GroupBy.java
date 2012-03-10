package org.basex.query.gflwor;

import static org.basex.query.QueryText.*;

import java.io.*;

import org.basex.io.serial.*;
import org.basex.query.*;
import org.basex.query.expr.*;
import org.basex.query.func.*;
import org.basex.query.gflwor.GFLWOR.Eval;
import org.basex.query.item.*;
import org.basex.query.util.*;
import org.basex.util.*;


/**
 * The GFLWOR {@code group by} expression.
 *
 * @author Leo Woerteler
 */
public class GroupBy extends GFLWOR.Clause {
  /** Grouping specs. */
  private Spec[] by;
  /** Non-grouping variables. */
  Var[][] nongroup;

  /**
   * Constructor.
   * @param specs grouping specs
   * @param ngrp non-grouping variables
   */
  public GroupBy(final Spec[] specs, final Var[][] ngrp) {
    by = specs;
    nongroup = ngrp;
  }

  @Override
  Eval eval(final Eval sub) {
    return new Eval() {
      @Override
      public boolean next(final QueryContext ctx) throws QueryException {
        // TODO Auto-generated method stub
        return false;
      }
    };
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

  /**
   * Grouping spec.
   *
   * @author Leo Woerteler
   */
  public static class Spec extends Single {
    /** Grouping variable. */
    final Var var;

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
  }
}
