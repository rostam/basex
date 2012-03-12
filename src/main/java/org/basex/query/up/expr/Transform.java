package org.basex.query.up.expr;

import static org.basex.query.QueryText.*;
import static org.basex.query.util.Err.*;

import java.io.IOException;
import org.basex.data.MemData;
import org.basex.io.serial.Serializer;
import org.basex.query.*;
import org.basex.query.expr.Arr;
import org.basex.query.expr.Expr;
import org.basex.query.gflwor.*;
import org.basex.query.item.ANode;
import org.basex.query.item.DBNode;
import org.basex.query.item.Item;
import org.basex.query.item.Value;
import org.basex.query.iter.Iter;
import org.basex.query.iter.ValueIter;
import org.basex.query.up.ContextModifier;
import org.basex.query.up.TransformModifier;
import org.basex.query.util.*;
import org.basex.query.var.*;
import org.basex.util.InputInfo;

/**
 * Transform expression.
 *
 * @author BaseX Team 2005-12, BSD License
 * @author Lukas Kircher
 */
public final class Transform extends Arr {
  /** Variable bindings created by copy clause. */
  private final Let[] copies;

  /**
   * Constructor.
   * @param ii input info
   * @param fl copy expressions
   * @param m modify expression
   * @param r return expression
   */
  public Transform(final InputInfo ii, final Let[] fl, final Expr m,
      final Expr r) {
    super(ii, m, r);
    copies = fl;
  }

  @Override
  public Expr comp(final QueryContext ctx, final VarScope scp) throws QueryException {
    final boolean u = ctx.updating();
    ctx.updating(true);

    for(final Let c : copies) c.expr = checkUp(c.expr, ctx).comp(ctx, scp);
    for(int e = 0; e != expr.length; ++e) expr[e] = expr[e].comp(ctx, scp);

    if(!expr[0].uses(Use.UPD) && !expr[0].isVacuous()) UPEXPECTT.thrw(input);
    checkUp(expr[1], ctx);
    ctx.updating(u);
    return this;
  }

  @Override
  public ValueIter iter(final QueryContext ctx) throws QueryException {
    return value(ctx).iter();
  }

  @Override
  public Value value(final QueryContext ctx) throws QueryException {
    final TransformModifier pu = new TransformModifier();
    for(final Let fo : copies) {
      final Iter ir = ctx.iter(fo.expr);
      final Item i = ir.next();
      if(i == null || !i.type.isNode() || ir.next() != null)
        UPCOPYMULT.thrw(input);

      // copy node to main memory data instance
      final MemData md = new MemData(ctx.context.prop);
      new DataBuilder(md).build((ANode) i);

      // add resulting node to variable
      ctx.set(fo.var, new DBNode(md), input);
      pu.addData(md);
    }

    final ContextModifier tmp = ctx.updates.mod;
    ctx.updates.mod = pu;
    ctx.value(expr[0]);
    ctx.updates.apply();
    ctx.updates.mod = tmp;

    return ctx.value(expr[1]);
  }

  @Override
  public boolean uses(final Use u) {
    return u == Use.VAR || u != Use.UPD && super.uses(u);
  }

  @Override
  public boolean removable(final Var v) {
    for(final Let c : copies) if(!c.removable(v)) return false;
    return super.removable(v);
  }

  @Override
  public Expr remove(final Var v) {
    for(final Let c : copies) c.remove(v);
    return super.remove(v);
  }

  @Override
  public void plan(final Serializer ser) throws IOException {
    ser.openElement(this);
    for(final Expr c : copies) c.plan(ser);
    for(final Expr e : expr) e.plan(ser);
    ser.closeElement();
  }

  @Override
  public String toString() {
    final StringBuilder sb = new StringBuilder(COPY + ' ');
    for(final Let t : copies)
      sb.append(t.var + " " + ASSIGN + ' ' + t.expr + ' ');
    return sb.append(MODIFY + ' ' + expr[0] + ' ' + RETURN + ' ' +
        expr[1]).toString();
  }

  @Override
  public boolean visitVars(final VarVisitor visitor) {
    if(!(visitor.visitAll(copies) && visitor.visitAll(expr))) return false;
    for(int i = copies.length; --i >= 0;)
      if(!visitor.undeclared(copies[i].var)) return false;
    return true;
  }
}
