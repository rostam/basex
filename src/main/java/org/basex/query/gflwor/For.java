package org.basex.query.gflwor;

import java.io.*;

import static org.basex.query.QueryText.*;

import org.basex.io.serial.*;
import org.basex.query.*;
import org.basex.query.expr.*;
import org.basex.query.gflwor.GFLWOR.Eval;
import org.basex.query.item.Dbl;
import org.basex.query.item.Empty;
import org.basex.query.item.Int;
import org.basex.query.item.Item;
import org.basex.query.iter.Iter;
import org.basex.query.util.*;
import org.basex.util.*;

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
  Expr expr;
  /** {@code allowing empty} flag. */
  final boolean empty;

  /**
   * Constructor.
   * @param v item variable
   * @param p position variable or {@code null}
   * @param s score variable or {@code null}
   * @param e bound expression
   * @param emp {@code allowing empty} flag
   * @param ii input info
   */
  public For(final Var v, final Var p, final Var s, final Expr e, final boolean emp,
      final InputInfo ii) {
    super(ii);
    var = v;
    pos = p;
    score = s;
    expr = e;
    empty = emp;
  }

  @Override
  Eval eval(final Eval sub) {
    return new Eval() {
      /** Expression iterator. */
      private Iter iter = Empty.ITER;
      /** Current position. */
      private long p;
      @Override
      public boolean next(final QueryContext ctx) throws QueryException {
        while(true) {
          final Item it = iter.next();
          if(it != null) {
            // there's another item to serve
            ctx.set(var, it, input);
            if(pos != null) ctx.set(pos, Int.get(++p), input);
            if(score != null) ctx.set(score, Dbl.get(it.score()), input);
            return true;
          } else if(empty && p == 0) {
            // expression yields no items, bind the empty sequence instead
            ctx.set(var, Empty.SEQ, input);
            if(pos != null) ctx.set(pos, Int.get(p++), input);
            if(score != null) ctx.set(score, Dbl.get(0), input);
            iter = Empty.ITER;
            return true;
          } else if(!sub.next(ctx)) {
            // no more iterations from above, we're done here
            return false;
          }

          // next iteration, reset iterator and counter
          iter = expr.iter(ctx);
          p = 0;
        }
      }
    };
  }

  @Override
  public void plan(final Serializer ser) throws IOException {
    ser.openElement(this);
    if(empty) ser.attribute(Token.token(EMPTYORD), Token.TRUE);
    var.plan(ser);
    if(pos != null) {
      ser.openElement(Token.token(QueryText.AT));
      pos.plan(ser);
      ser.closeElement();
    }

    if(score != null) {
      ser.openElement(Token.token(QueryText.SCORE));
      score.plan(ser);
      ser.closeElement();
    }

    expr.plan(ser);
    ser.closeElement();
  }

  @Override
  public String toString() {
    final StringBuilder sb = new StringBuilder(FOR).append(' ').append(var);
    if(empty) sb.append(' ').append(ALLOWING).append(' ').append(EMPTYORD);
    if(pos != null) sb.append(' ').append(AT).append(' ').append(pos);
    if(score != null) sb.append(' ').append(SCORE).append(' ').append(score);
    return sb.append(' ').append(IN).append(' ').append(expr).toString();
  }

  @Override
  public boolean uses(final Use u) {
    return u == Use.VAR || expr.uses(u);
  }

  @Override
  public For comp(final QueryContext ctx) throws QueryException {
    expr = expr.comp(ctx);
    return this;
  }

  @Override
  public boolean removable(final Var v) {
    return expr.removable(v);
  }

  @Override
  public Expr remove(final Var v) {
    expr = expr.remove(v);
    return this;
  }

  @Override
  public boolean visitVars(final VarVisitor visitor) {
    return expr.visitVars(visitor) && visitor.declared(var)
        && (pos == null || visitor.declared(pos))
        && (score == null || visitor.declared(score));
  }

  @Override
  boolean undeclare(final VarVisitor visitor) {
    return (score == null || visitor.undeclared(score))
        && (pos == null || visitor.undeclared(pos)) && visitor.undeclared(var);
  }

  @Override
  public Var[] vars() {
    return pos == null ? score == null ? new Var[] { var } : new Var[] { var, score } :
      score == null ? new Var[] { var, pos } : new Var[] { var, pos, score };
  }

  @Override
  public boolean declares(final Var v) {
    return var.is(v) || pos != null && pos.is(v) || score != null && score.is(v);
  }
}
