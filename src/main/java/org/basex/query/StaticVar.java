package org.basex.query;

import static org.basex.query.QueryText.*;
import static org.basex.query.util.Err.*;
import java.io.*;
import org.basex.io.serial.*;
import org.basex.query.expr.*;
import org.basex.query.item.*;
import org.basex.query.util.*;
import org.basex.util.*;

/**
 * Static variable which can be assigned an expression.
 *
 * @author BaseX Team 2005-12, BSD License
 * @author Leo Woerteler
 */
public final class StaticVar extends ParseExpr {
  /** Declared variable. */
  public final Var var;
  /** Annotations. */
  public final Ann ann;

  /** Declaration flag. */
  public boolean declared;

  /** Bound value. */
  private Value value;
  /** Bound expression. */
  private Expr expr;

  /**
   * Constructor.
   * @param ii input info
   * @param v variable
   * @param a annotations
   * @param e expression to be bound
   */
  StaticVar(final InputInfo ii, final Var v, final Ann a, final Expr e) {
    super(ii);
    var = v;
    ann = a;
    type = v.type();
    expr = e;
  }

  /**
   * Checks if the variable contains no updating expression.
   * @throws QueryException query exception
   */
  public void checkUp() throws QueryException {
    if(expr != null && expr.uses(Use.UPD)) UPNOT.thrw(input, description());
  }

  @Override
  public StaticVar comp(final QueryContext ctx, final VarScope scp)
      throws QueryException {
    if(expr != null) bind(checkUp(expr, ctx).comp(ctx, scp), ctx);
    return this;
  }

  /**
   * Sets the specified variable type.
   * @param t type
   * @param ctx query context
   * @throws QueryException query exception
   */
  public void reset(final SeqType t, final QueryContext ctx)
      throws QueryException {

    type = t;
    if(value != null && !value.type.instanceOf(t.type) &&
        value instanceof Item) {
      value = type.type.cast((Item) value, ctx, input);
    }
  }

  /**
   * Binds the specified expression to the variable.
   * @param e expression to be set
   * @param ctx query context
   * @return self reference
   * @throws QueryException query exception
   */
  public StaticVar bind(final Expr e, final QueryContext ctx) throws QueryException {
    expr = e;
    return e.isValue() ? bind((Value) e, ctx) : this;
  }

  /**
   * Returns the bound expression.
   * @return expression
   */
  public Expr expr() {
    return expr;
  }

  /**
   * Binds the specified value to the variable.
   * @param v value to be set
   * @param ctx query context
   * @return self reference
   * @throws QueryException query exception
   */
  public StaticVar bind(final Value v, final QueryContext ctx) throws QueryException {
    expr = v;
    value = cast(v, ctx);
    return this;
  }

  /**
   * If necessary, casts the specified value if a type is specified.
   * @param v input value
   * @param ctx query context
   * @return cast value
   * @throws QueryException query exception
   */
  private Value cast(final Value v, final QueryContext ctx)
      throws QueryException {
    return type == null ? v : type.promote(v, ctx, input);
  }

  @Override
  public void plan(final Serializer ser) throws IOException {
    ser.openElement(this, NAM, Token.token(toString()));
    if(expr != null) expr.plan(ser);
    ser.closeElement();
  }

  @Override
  public SeqType type() {
    return type != null ? type : var.type();
  }

  @Override
  public boolean sameAs(final Expr cmp) {
    if(!(cmp instanceof StaticVar)) return false;
    final StaticVar v = (StaticVar) cmp;
    return var.equals(v.var) && type().eq(v.type());
  }

  @Override
  public boolean uses(final Use u) {
    return expr != null && expr.uses(u);
  }

  @Override
  public boolean removable(final Var v) {
    return false;
  }

  @Override
  public Expr remove(final Var v) {
    return this;
  }

  @Override
  public String toString() {
    final StringBuilder sb = new StringBuilder(DECLARE).append(' ');
    if(!ann.isEmpty()) sb.append(ann).append(' ');
    sb.append(VARIABLE).append(' ').append(var).append(' ');
    if(expr != null) sb.append(ASSIGN).append(' ').append(expr);
    else sb.append(EXTERNAL);
    return sb.append(';').toString();
  }

  @Override
  public boolean visitVars(final VarVisitor visitor) {
    return expr.visitVars(visitor);
  }
}
