package org.basex.query.expr;

import static org.basex.query.QueryText.*;
import java.io.IOException;

import org.basex.io.serial.Serializer;
import org.basex.query.*;
import org.basex.query.item.*;
import org.basex.query.iter.Iter;
import org.basex.query.util.*;
import org.basex.query.var.*;
import org.basex.util.InputInfo;
import org.basex.util.Token;
import org.basex.util.TokenBuilder;
import org.basex.util.list.*;

/**
 * Case expression for typeswitch.
 *
 * @author BaseX Team 2005-12, BSD License
 * @author Christian Gruen
 */
public final class TypeCase extends Single {
  /** Variable. */
  final Var var;
  /** Matched sequence types. */
  final SeqType[] types;

  /**
   * Constructor.
   * @param ii input info
   * @param v variable
   * @param ts sequence types this case matches, the empty array means {@code default}
   * @param r return expression
   */
  public TypeCase(final InputInfo ii, final Var v, final SeqType[] ts, final Expr r) {
    super(ii, r);
    var = v;
    types = ts;
  }

  @Override
  public TypeCase comp(final QueryContext ctx, final VarScope scp) throws QueryException {
    return comp(ctx, scp, (Value) null);
  }

  /**
   * Compiles the expression.
   * @param ctx query context
   * @param scp variable scope
   * @param v value to be bound
   * @return resulting item
   * @throws QueryException query exception
   */
  TypeCase comp(final QueryContext ctx, final VarScope scp, final Value v)
      throws QueryException {
    if(var != null && v != null) ctx.set(var, v, input);
    super.comp(ctx, scp);
    type = expr.type();
    return this;
  }

  @Override
  public boolean uses(final Use u) {
    return u == Use.VAR || super.uses(u);
  }

  /**
   * Checks if the given value matches this case.
   * @param val value to be matched
   * @return {@code true} if it matches, {@code false} otherwise
   */
  public boolean matches(final Value val) {
    if(types.length == 0) return true;
    for(final SeqType t : types) if(t.instance(val)) return true;
    return false;
  }

  /**
   * Evaluates the expression.
   * @param ctx query context
   * @param seq sequence to be checked
   * @return resulting item
   * @throws QueryException query exception
   */
  Iter iter(final QueryContext ctx, final Value seq) throws QueryException {
    if(!matches(seq)) return null;

    if(var == null) return ctx.iter(expr);
    ctx.set(var, seq, input);
    return ctx.value(expr).iter();
  }

  @Override
  public void plan(final Serializer ser) throws IOException {
    ser.openElement(this);
    if(types.length == 0) {
      ser.attribute(Token.token(DEFAULT), Token.TRUE);
    } else {
      final ByteList bl = new ByteList();
      for(final SeqType t : types) bl.add(Token.token(t + " | "));
      bl.size(bl.size() - 3);
      ser.attribute(Token.token(TYPE), bl.toArray());
    }
    if(var != null) ser.attribute(VAR, Token.token(var.toString()));
    expr.plan(ser);
    ser.closeElement();
  }

  @Override
  public String toString() {
    final TokenBuilder tb = new TokenBuilder(types.length == 0 ? DEFAULT : CASE);
    if(var != null) {
      tb.add(' ').add(var.toString());
      if(types.length != 0) tb.add(' ').add(AS);
    }
    if(types.length != 0) {
      for(int i = 0; i < types.length; i++) {
        if(i > 0) tb.add(" |");
        tb.add(' ').add(types[i].toString());
      }
    }
    return tb.add(" " + RETURN + ' ' + expr).toString();
  }

  @Override
  public TypeCase markTailCalls() {
    expr = expr.markTailCalls();
    return this;
  }

  @Override
  public boolean visitVars(final VarVisitor visitor) {
    return var == null ? expr.visitVars(visitor)
        : visitor.declared(var) && expr.visitVars(visitor) && visitor.undeclared(var);
  }
}
