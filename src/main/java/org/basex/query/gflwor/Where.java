package org.basex.query.gflwor;

import org.basex.query.QueryContext;
import org.basex.query.QueryException;
import org.basex.query.expr.Expr;
import org.basex.query.gflwor.GFLWOR.Eval;
import org.basex.util.InputInfo;


/**
 * GFLWOR {@code where} clause, filtering tuples not satisfying the predicate.
 *
 * @author Leo Woerteler
 */
public class Where extends GFLWOR.Clause {
  /** Predicate expression. */
  final Expr pred;
  /** Input info. */
  final InputInfo input;

  /**
   * Constructor.
   * @param ii input info
   * @param e predicate expression
   */
  public Where(final InputInfo ii, final Expr e) {
    input = ii;
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
}
