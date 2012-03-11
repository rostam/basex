package org.basex.query.func;

import static org.basex.query.QueryText.*;

import java.io.*;
import java.util.*;
import java.util.Map.Entry;

import org.basex.io.serial.*;
import org.basex.query.*;
import org.basex.query.expr.*;
import org.basex.query.item.*;
import org.basex.query.iter.*;
import org.basex.query.util.*;
import org.basex.util.*;

/**
 * Inline function.
 *
 * @author BaseX Team 2005-12, BSD License
 * @author Leo Woerteler
 */
public final class InlineFunc extends UserFunc {
  /**
   * Constructor.
   * @param ii input info
   * @param r return type
   * @param v arguments
   * @param e function body
   * @param a annotations
   * @param scp scope
   */
  public InlineFunc(final InputInfo ii, final SeqType r, final Var[] v,
      final Expr e, final Ann a, final VarScope scp) {
    super(ii, null, v, r, a, scp);
    expr = e;
  }

  @Override
  public Expr comp(final QueryContext ctx, final VarScope scp) throws QueryException {
    cmp(ctx);
    // only evaluate if the closure is empty, so we don't lose variables
    return expr.hasFreeVars() ? this : preEval(ctx);
  }

  @Override
  public FuncItem item(final QueryContext ctx, final InputInfo ii) throws QueryException {
    final FuncType ft = FuncType.get(this);
    final boolean c = ft.ret != null && !expr.type().instance(ft.ret);

    // collect closure
    final Map<Var, Value> clos = new HashMap<Var, Value>();
    for(final Entry<Var, VarRef> e : scope.closure().entrySet())
      clos.put(e.getKey(), e.getValue().value(ctx));

    return new FuncItem(args, expr, ft, c, clos, scope.stackSize());
  }

  @Override
  public Value value(final QueryContext ctx) throws QueryException {
    return item(ctx, input);
  }

  @Override
  public ValueIter iter(final QueryContext ctx) throws QueryException {
    return value(ctx).iter();
  }

  @Override
  public boolean uses(final Use u) {
    return u == Use.X30 || super.uses(u);
  }

  @Override
  public boolean removable(final Var v) {
    return false;
  }

  @Override
  public Expr remove(final Var v) {
    throw Util.notexpected(v);
  }

  @Override
  public void plan(final Serializer ser) throws IOException {
    ser.openElement(this);
    for(int i = 0; i < args.length; ++i) {
      ser.attribute(Token.token(ARG + i), args[i].name.string());
    }
    expr.plan(ser);
    ser.closeElement();
  }

  @Override
  public String toString() {
    final StringBuilder sb = new StringBuilder(FUNCTION).append(PAR1);
    for(int i = 0; i < args.length; i++) {
      if(i > 0) sb.append(", ");
      sb.append(args[i].toString());
    }
    sb.append(PAR2).append(' ');
    if(ret != null) sb.append("as ").append(ret.toString()).append(' ');
    return sb.append("{ ").append(expr).append(" }").toString();
  }

  @Override
  boolean tco() {
    return false;
  }

  @Override
  public boolean visit(final VarVisitor visitor) {
    final Map<Var, VarRef> clos = scope.closure();
    if(clos.isEmpty()) return visitor.withVars(args, expr);

    final Var[] cls = new Var[clos.size()];
    int i = cls.length;
    for(final Entry<Var, VarRef> v : clos.entrySet()) {
      if(!(visitor.used(v.getValue())) && visitor.declared(v.getKey())) return false;
      cls[--i] = v.getKey();
    }
    if(!visitor.withVars(args, expr)) return false;
    for(final Var v : cls) if(!visitor.undeclared(v)) return false;
    return true;
  }
}
