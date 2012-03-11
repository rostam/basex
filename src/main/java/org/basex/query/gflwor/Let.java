package org.basex.query.gflwor;

import java.io.*;

import org.basex.io.serial.*;
import static org.basex.query.QueryText.*;
import org.basex.query.*;
import org.basex.query.expr.*;
import org.basex.query.gflwor.GFLWOR.Eval;
import org.basex.query.item.Dbl;
import org.basex.query.item.Item;
import org.basex.query.item.Value;
import org.basex.query.iter.Iter;
import org.basex.query.util.*;
import org.basex.util.*;
import org.basex.util.ft.Scoring;


/**
 * FLWOR {@code let} clause, binding an expression to a variable.
 * @author Leo Woerteler
 */
public class Let extends GFLWOR.Clause {
  /** Variable. */
  public final Var var;
  /** Bound expression. */
  public Expr expr;
  /** Score flag. */
  final boolean score;

  /**
   * Constructor.
   * @param v variable
   * @param e expression
   * @param scr score flag
   * @param ii input info
   */
  public Let(final Var v, final Expr e, final boolean scr, final InputInfo ii) {
    super(ii);
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

  @Override
  public boolean uses(final Use u) {
    return u == Use.VAR || expr.uses(u);
  }

  @Override
  public Let comp(final QueryContext ctx) throws QueryException {
    expr = expr.comp(ctx);
    return this;
  }

  @Override
  public boolean removable(final Var v) {
    return expr.removable(v);
  }

  @Override
  public Let remove(final Var v) {
    expr = expr.remove(v);
    return this;
  }

  @Override
  public boolean visitVars(final VarVisitor visitor) {
    return expr.visitVars(visitor) && visitor.declared(var);
  }

  @Override
  boolean undeclare(final VarVisitor visitor) {
    return visitor.undeclared(var);
  }

  @Override
  public Var[] vars() {
    return new Var[] { var };
  }

  @Override
  public boolean declares(final Var v) {
    return var.is(v);
  }
}
