package org.basex.query.gflwor;

import java.util.ArrayList;

import org.basex.data.*;
import org.basex.query.QueryContext;
import org.basex.query.QueryException;
import org.basex.query.expr.Expr;
import org.basex.query.item.Empty;
import org.basex.query.item.Item;
import org.basex.query.iter.Iter;

/**
 * General FLWOR expression.
 * @author Leo Woerteler
 */
public class GFLWOR {
  /** Return expression. */
  Expr ret;
  /** FLWOR clauses. */
  private ArrayList<Clause> clauses;

  /**
   * Iterator.
   * @param ctx query context
   * @return iterator
   * @throws QueryException exception
   */
  public Iter iter(final QueryContext ctx) throws QueryException {
    Eval e = start();
    for(final Clause cls : clauses) e = cls.eval(e);
    if(!e.next(ctx)) return Empty.ITER;
    final Eval ev = e;
    return new Iter() {
      /** Return iterator. */
      Iter sub = ret.iter(ctx);
      @Override
      public Item next() throws QueryException {
        if(sub == null) return null;
        while(true) {
          final Item it = sub.next();
          if(it != null) return it;
          if(!ev.next(ctx)) {
            sub = null;
            return null;
          }
          sub = ret.iter(ctx);
        }
      }
    };
  }

  /**
   * Start evaluator, doing nothing, once.
   * @return evaluator
   */
  private Eval start() {
    return new Eval() {
      /** First-evaluation flag. */
      private boolean first;
      @Override
      public boolean next(final QueryContext ctx) {
        if(!first) return false;
        first = false;
        return true;
      }
    };
  }

  /**
   * Evaluator for FLWOR clauses.
   *
   * @author Leo Woerteler
   */
  interface Eval {
    /**
     * Makes the next evaluation step if available. This method is guaranteed
     * to not be called again if it has once returned {@code false}.
     * @param ctx query context
     * @return {@code true} if step was made, {@code false} if no more
     * results exist
     * @throws QueryException evaluation exception
     */
    boolean next(final QueryContext ctx) throws QueryException;
  }

  /**
   * A FLWOR clause.
   * @author Leo Woerteler
   */
  abstract static class Clause extends ExprInfo {
    /**
     * Evaluates the clause.
     * @param sub wrapped evaluator
     * @return evaluator
     */
    abstract Eval eval(final Eval sub);
  }
}
