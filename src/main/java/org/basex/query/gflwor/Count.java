package org.basex.query.gflwor;

import java.io.*;

import org.basex.io.serial.*;
import org.basex.query.*;
import org.basex.query.expr.*;
import org.basex.query.gflwor.GFLWOR.Eval;
import org.basex.query.item.Int;
import org.basex.query.util.*;
import org.basex.query.var.*;
import org.basex.util.*;


/**
 * GFLWOR {@code count} clause.
 * @author Leo Woerteler
 */
public class Count extends GFLWOR.Clause {
  /** Count variable. */
  final Var count;

  /**
   * Constructor.
   * @param v variable
   * @param ii input info
   */
  public Count(final Var v, final InputInfo ii) {
    super(ii);
    count = v;
  }

  @Override
  Eval eval(final Eval sub) {
    return new Eval() {
      /** Counter. */
      private long i = 1;
      @Override
      public boolean next(final QueryContext ctx) throws QueryException {
        if(!sub.next(ctx)) return false;
        ctx.set(count, Int.get(i++), input);
        return true;
      }
    };
  }

  @Override
  public void plan(final Serializer ser) throws IOException {
    ser.openElement(this);
    count.plan(ser);
    ser.closeElement();
  }

  @Override
  public String toString() {
    return "count " + count;
  }

  @Override
  public boolean uses(final Use u) {
    return u == Use.VAR || u == Use.X30;
  }

  @Override
  public Count comp(final QueryContext ctx, final VarScope scp) throws QueryException {
    return this;
  }

  @Override
  public boolean removable(final Var v) {
    return true;
  }

  @Override
  public Expr remove(final Var v) {
    return this;
  }

  @Override
  public boolean visitVars(final VarVisitor visitor) {
    return visitor.declared(count);
  }

  @Override
  boolean undeclare(final VarVisitor visitor) {
    return visitor.undeclared(count);
  }

  @Override
  public Var[] vars() {
    return new Var[] { count };
  }

  @Override
  public boolean declares(final Var v) {
    return count.is(v);
  }
}
