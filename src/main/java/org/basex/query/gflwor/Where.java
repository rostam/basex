package org.basex.query.gflwor;

import java.io.*;

import org.basex.io.serial.*;
import org.basex.query.*;
import org.basex.query.expr.*;
import org.basex.query.gflwor.GFLWOR.Eval;
import org.basex.query.util.*;
import org.basex.query.var.*;
import org.basex.util.InputInfo;


/**
 * GFLWOR {@code where} clause, filtering tuples not satisfying the predicate.
 *
 * @author BaseX Team 2005-12, BSD License
 * @author Leo Woerteler
 */
public class Where extends GFLWOR.Clause {
  /** Predicate expression. */
  Expr pred;

  /**
   * Constructor.
   * @param ii input info
   * @param e predicate expression
   */
  public Where(final Expr e, final InputInfo ii) {
    super(ii);
    pred = e;
  }

  @Override
  Eval eval(final Eval sub) {
    return new Eval() {
      @Override
      public boolean next(final QueryContext ctx) throws QueryException {
        while(sub.next(ctx)) if(pred.ebv(ctx, input).bool(input)) return true;
        return false;
      }
    };
  }

  @Override
  public void plan(final Serializer ser) throws IOException {
    ser.openElement(this);
    pred.plan(ser);
    ser.closeElement();
  }

  @Override
  public String toString() {
    return QueryText.WHERE + ' ' + pred;
  }

  @Override
  public boolean uses(final Use u) {
    return pred.uses(u);
  }

  @Override
  public Where comp(final QueryContext ctx, final VarScope scp) throws QueryException {
    pred = pred.comp(ctx, scp);
    return this;
  }

  @Override
  public boolean removable(final Var v) {
    return pred.removable(v);
  }

  @Override
  public Expr remove(final Var v) {
    pred = pred.remove(v);
    return this;
  }

  @Override
  public boolean visitVars(final VarVisitor visitor) {
    return pred.visitVars(visitor);
  }
}
