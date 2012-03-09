package org.basex.query.gflwor;

import org.basex.query.QueryContext;
import org.basex.query.QueryException;
import org.basex.query.gflwor.GFLWOR.Eval;
import org.basex.query.item.Int;
import org.basex.query.util.Var;


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
   */
  public Count(final Var v) {
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
        ctx.set(count, Int.get(i++));
        return true;
      }
    };
  }
}
