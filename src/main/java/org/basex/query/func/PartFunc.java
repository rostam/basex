package org.basex.query.func;

import static org.basex.query.QueryText.*;
import java.io.IOException;
import java.util.*;
import java.util.Map.*;

import org.basex.io.serial.Serializer;
import org.basex.query.*;
import org.basex.query.expr.*;
import org.basex.query.item.*;
import org.basex.query.util.*;
import org.basex.util.InputInfo;
import org.basex.util.Token;

/**
 * Partial function application.
 *
 * @author BaseX Team 2005-12, BSD License
 * @author Leo Woerteler
 */
public final class PartFunc extends UserFunc {
  /**
   * Function constructor for static calls.
   * @param ii input info
   * @param fun typed function expression
   * @param arg arguments
   * @param scp scope
   * @throws QueryException exception
   */
  public PartFunc(final InputInfo ii, final TypedFunc fun, final Var[] arg,
      final VarScope scp) throws QueryException {
    super(ii, new QNm(), type(arg, fun.type), fun.ret(), null, scp);
    expr = fun.fun;
  }

  /**
   * Function constructor for dynamic calls.
   * @param ii input info
   * @param func function expression
   * @param arg arguments
   * @param scp scope
   */
  public PartFunc(final InputInfo ii, final Expr func, final Var[] arg,
      final VarScope scp) {
    // [LW] XQuery/HOF: dynamic type propagation
    super(ii, new QNm(), arg, func.type(), null, scp);
    expr = func;
  }

  /**
   * Sets the types of the given variables.
   * @param vars variables to type
   * @param ft function type
   * @return the variables for convenience
   * @throws QueryException exception
   */
  public static Var[] type(final Var[] vars, final FuncType ft) throws QueryException {
    if(ft != FuncType.ANY_FUN) {
      for(int v = 0; v < vars.length; v++)
        if(vars[v] != null && ft.args[v] != SeqType.ITEM_ZM)
          vars[v].setRetType(ft.args[v]);
    }
    return vars;
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
  public Expr comp(final QueryContext ctx) throws QueryException {
    cmp(ctx);
    // defer creation of function item because of closure
    return new InlineFunc(input, ret, args, expr, ann, scope).comp(ctx);
  }

  @Override
  public String toString() {
    final StringBuilder sb = new StringBuilder(FUNCTION).append('(');
    for(final Var v : args)
      sb.append(v).append(v == args[args.length - 1] ? "" : ", ");
    return sb.append(") { ").append(expr).append(" }").toString();
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
