package org.basex.query.expr;

import static org.basex.query.QueryText.*;
import java.io.IOException;

import org.basex.io.serial.Serializer;
import org.basex.query.*;
import org.basex.query.func.UserFuncCall;
import org.basex.query.item.Item;
import org.basex.query.item.Value;
import org.basex.query.iter.Iter;
import org.basex.query.util.*;
import org.basex.util.InputInfo;
import org.basex.util.TokenBuilder;

/**
 * Variable Reference expression.
 *
 * @author BaseX Team 2005-12, BSD License
 * @author Christian Gruen
 */
public final class VarRef extends ParseExpr {
  /** Variable name. */
  public Var var;

  /**
   * Constructor.
   * @param ii input info
   * @param v variable
   */
  public VarRef(final InputInfo ii, final Var v) {
    super(ii);
    var = v;
  }

  @Override
  public Expr comp(final QueryContext ctx, final VarScope scp) throws QueryException {

    type = var.type();
    size = var.size;

    Expr e = null; // [LW] ctx.getExpr(var);
    // return if variable expression has not yet been assigned
    if(e == null) return this;

    /* Choose expressions to be pre-evaluated.
     * If a variable is pre-evaluated, it may not be available for further
     * optimizations (index access, count, ...). On the other hand, repeated
     * evaluation of the same expression is avoided.
     *
     * [CG][LW] Variables are currently pre-evaluated if...
     * - they are global (mandatory)
     * - namespaces are used
     * - they are given a type
     * - they contain an element constructor (mandatory)
     * - they contain a function call
     */
    if(var.global() || var.checksType() || e.uses(Use.CNS) || e instanceof UserFuncCall) {
      e = ctx.get(var);
    }

    return e;
  }

  @Override
  public Item item(final QueryContext ctx, final InputInfo ii)
      throws QueryException {
    return ctx.get(var).item(ctx, ii);
  }

  @Override
  public Iter iter(final QueryContext ctx) throws QueryException {
    return ctx.get(var).iter();
  }

  @Override
  public Value value(final QueryContext ctx) throws QueryException {
    return ctx.get(var);
  }

  @Override
  public boolean uses(final Use u) {
    return u == Use.VAR;
  }

  @Override
  public boolean removable(final Var v) {
    return true;
  }

  @Override
  public Expr remove(final Var v) {
    return var.is(v) ? new Context(input) : this;
  }

  @Override
  public boolean sameAs(final Expr cmp) {
    return cmp instanceof VarRef && var.is(((VarRef) cmp).var);
  }

  @Override
  public void plan(final Serializer ser) throws IOException {
    ser.openElement(this);
    var.plan(ser);
    ser.closeElement();
  }

  @Override
  public String description() {
    return VARBL;
  }

  @Override
  public String toString() {
    return new TokenBuilder(DOLLAR).add(var.name.string()).toString();
  }

  @Override
  public boolean visitVars(final VarVisitor visitor) {
    return visitor.used(this);
  }
}
