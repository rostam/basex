package org.basex.query.gflwor;

import org.basex.query.QueryContext;
import org.basex.query.QueryException;
import org.basex.query.gflwor.GFLWOR.Eval;


/**
 * The GFLWOR {@code group by} expression.
 *
 * @author Leo Woerteler
 */
public class GroupBy extends GFLWOR.Clause {

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
}
