package org.basex.query.var;

import static org.basex.query.QueryText.*;
import static org.basex.query.util.Err.*;
import java.io.*;
import org.basex.io.serial.*;
import org.basex.query.*;
import org.basex.query.expr.*;
import org.basex.query.item.*;
import org.basex.query.iter.*;
import org.basex.query.util.*;
import org.basex.util.*;

/**
 * Static variable which can be assigned an expression.
 *
 * @author BaseX Team 2005-12, BSD License
 * @author Leo Woerteler
 */
public final class GlobalVar extends VarRef {
  /** Annotations. */
  public final Ann ann;
  /** Declaration flag. */
  public boolean declared;
  /** Bound value. */
  private Value value;
  /** Bound expression. */
  private Expr expr;

  /** Variables should only be compiled once. */
  private boolean compiled;

  /**
   * Constructor.
   * @param ii input info
   * @param a annotations
   * @param n variable name
   * @param t variable type
   * @param e expression to be bound
   */
  GlobalVar(final InputInfo ii, final Ann a, final QNm n, final SeqType t, final Expr e) {
    super(n, ii);
    ann = a == null ? new Ann() : a;
    type = t;
    expr = e;
  }

  /**
   * Checks if the variable contains no updating expression.
   * @throws QueryException query exception
   */
  public void checkUp() throws QueryException {
    if(expr != null && expr.uses(Use.UPD)) UPNOT.thrw(info, description());
  }

  @Override
  public Value comp(final QueryContext ctx, final VarScope scp)
      throws QueryException {
    if(compiled) {
      if(value == null) throw Err.CIRCVAR.thrw(info, this);
      return value;
    }
    if(expr == null) throw Err.VARUNDEF.thrw(info, this);

    compiled = true;
    return bind(checkUp(expr, ctx).comp(ctx, scp).value(ctx), ctx);
  }

  @Override
  public Iter iter(final QueryContext ctx) throws QueryException {
    return value(ctx).iter();
  }

  @Override
  public Value value(final QueryContext ctx) throws QueryException {
    if(value != null) return value;
    if(expr == null) throw Err.VARUNDEF.thrw(info, this);
    return bind(expr.value(ctx), ctx);
  }

  /**
   * Sets the specified variable type.
   * @param t type
   * @param ctx query context
   * @throws QueryException query exception
   */
  public void reset(final SeqType t, final QueryContext ctx) throws QueryException {
    type = t;
    if(value != null && !value.type.instanceOf(t.type) &&
        value instanceof Item) {
      value = type.type.cast((Item) value, ctx, info);
    }
  }

  /**
   * Binds the specified expression to the variable.
   * @param e expression to be set
   * @param ctx query context
   * @return self reference
   * @throws QueryException query exception
   */
  public Expr bind(final Expr e, final QueryContext ctx) throws QueryException {
    expr = e;
    if(e.isValue()) return bind((Value) e, ctx);
    compiled = false;
    return this;
  }

  /**
   * Binds the specified value to the variable.
   * @param v value to be set
   * @param ctx query context
   * @return self reference
   * @throws QueryException query exception
   */
  public Value bind(final Value v, final QueryContext ctx) throws QueryException {
    expr = v;
    value = cast(v, ctx);
    return value;
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
    return type == null ? v : v.isItem() ? type.cast((Item) v, true, ctx, info, this)
        : type.promote(v, ctx, info);
  }

  @Override
  public void plan(final Serializer ser) throws IOException {
    ser.openElement(this, NAM, name.string());
    if(expr != null) expr.plan(ser);
    ser.closeElement();
  }

  @Override
  public SeqType type() {
    return type != null ? type : SeqType.ITEM_ZM;
  }

  @Override
  public boolean sameAs(final Expr cmp) {
    if(!(cmp instanceof GlobalVar)) return false;
    final GlobalVar v = (GlobalVar) cmp;
    return name.equals(v.name) && type().eq(v.type());
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
  public boolean visitVars(final VarVisitor visitor) {
    return expr.visitVars(visitor);
  }

  /**
   * Tries to refine the compile-time type of this variable through the type of the bound
   * expression.
   * @param t type of the bound expression
   * @throws QueryException if the types are incompatible
   */
  @SuppressWarnings("unused")
  public void refineType(final SeqType t) throws QueryException {
    if(t == null) return;
    if(type == null) type = t;
    else {
      // [LW] insert checks here
      type = t;
    }
  }

  /**
   * Getter.
   * @return the bound expression
   */
  protected Expr expr() {
    return expr;
  }
}
