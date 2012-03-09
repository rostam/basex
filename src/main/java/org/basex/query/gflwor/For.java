package org.basex.query.gflwor;

import org.basex.query.QueryContext;
import org.basex.query.QueryException;
import org.basex.query.expr.Expr;
import org.basex.query.gflwor.GFLWOR.Eval;
import org.basex.query.item.Dbl;
import org.basex.query.item.Empty;
import org.basex.query.item.Int;
import org.basex.query.item.Item;
import org.basex.query.iter.Iter;
import org.basex.query.util.Var;

/**
 * FLWOR {@code for} clause, iterating over a sequence.
 * @author Leo Woerteler
 */
public class For extends GFLWOR.Clause {
  /** Item variable. */
  final Var var;
  /** Position variable. */
  final Var pos;
  /** Score variable. */
  final Var score;
  /** Bound expression. */
  final Expr expr;

  /**
   * Constructor.
   * @param v item variable
   * @param p position variable or {@code null}
   * @param s score variable or {@code null}
   * @param e bound expression
   */
  public For(final Var v, final Var p, final Var s, final Expr e) {
    var = v;
    pos = p;
    score = s;
    expr = e;
  }

  @Override
  Eval eval(final Eval sub) {
    return new Eval() {
      /** Expression iterator. */
      private Iter iter = Empty.ITER;
      /** Current position. */
      private long p = 1;
      @Override
      public boolean next(final QueryContext ctx) throws QueryException {
        while(true) {
          final Item it = iter.next();
          if(it != null) {
            ctx.set(var, it);
            if(pos != null) ctx.set(pos, Int.get(p++));
            if(score != null) ctx.set(score, Dbl.get(it.score()));
            return true;
          }

          if(!sub.next(ctx)) return false;
          iter = expr.iter(ctx);
        }
      }
    };
  }
}
