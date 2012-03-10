package org.basex.query.gflwor;

import java.io.*;

import org.basex.io.serial.*;
import static org.basex.query.QueryText.*;
import org.basex.query.*;
import org.basex.query.expr.Expr;
import org.basex.query.gflwor.GFLWOR.Eval;
import org.basex.query.item.Dbl;
import org.basex.query.item.Item;
import org.basex.query.item.Value;
import org.basex.query.iter.Iter;
import org.basex.query.util.Var;
import org.basex.util.*;
import org.basex.util.ft.Scoring;


/**
 * FLWOR {@code let} clause, binding an expression to a variable.
 * @author Leo Woerteler
 */
public class Let extends GFLWOR.Clause {
  /** Variable. */
  final Var var;
  /** Bound expression. */
  final Expr expr;
  /** Score flag. */
  final boolean score;

  /**
   * Constructor.
   * @param v variable
   * @param e expression
   * @param scr score flag
   */
  public Let(final Var v, final Expr e, final boolean scr) {
    var = v;
    expr = e;
    score = scr;
  }

  @Override
  Eval eval(final Eval sub) {
    return new Eval() {
      @Override
      public boolean next(final QueryContext ctx) throws QueryException {
        if(!sub.next(ctx)) return false;
        final Value bind;
        if(score) {
          final Iter iter = expr.iter(ctx);
          double sum = 0;
          int sz = 0;
          for(Item it; (it = iter.next()) != null; sum += it.score(), sz++);
          bind = Dbl.get(Scoring.let(sum, sz));
        } else {
          bind = ctx.value(expr);
        }
        ctx.set(var, bind);
        return true;
      }
    };
  }

  @Override
  public void plan(final Serializer ser) throws IOException {
    ser.openElement(this);
    if(score) ser.attribute(Token.token(SCORE), Token.TRUE);
    var.plan(ser);
    expr.plan(ser);
    ser.closeElement();
  }

  @Override
  public String toString() {
    return LET + ' ' + (score ? SCORE + ' ' : "") + var + ' ' + ASSIGN + ' ' + expr;
  }

}
