package org.basex.query.expr;

import static org.basex.query.QueryText.*;
import static org.basex.query.func.Function.*;
import java.io.IOException;
import java.util.*;

import org.basex.io.serial.Serializer;
import org.basex.query.*;
import org.basex.query.gflwor.*;
import org.basex.query.gflwor.GFLWOR.Clause;
import org.basex.query.item.Bln;
import org.basex.query.item.SeqType;
import org.basex.query.util.*;
import org.basex.query.var.*;
import org.basex.util.*;

/**
 * Some/Every satisfier clause.
 *
 * @author BaseX Team 2005-12, BSD License
 * @author Christian Gruen
 */
public final class Quantifier extends ParseExpr {
  /** Every flag. */
  private final boolean every;
  /** For/Let expressions. */
  private final For[] fl;
  /** Satisfier. */
  private Expr sat;

  /**
   * Constructor.
   * @param ii input info
   * @param f variable inputs
   * @param s satisfier
   * @param e every flag
   */
  public Quantifier(final InputInfo ii, final For[] f, final Expr s,
      final boolean e) {
    super(ii);
    sat = s;
    fl = f;
    every = e;
    type = SeqType.BLN;
  }

  @Override
  public Expr comp(final QueryContext ctx, final VarScope scp) throws QueryException {
    final GFLWOR flwor = new GFLWOR(input, new ArrayList<Clause>(Arrays.asList(fl)),
        BOOLEAN.get(input, sat));

    // return pre-evaluated result
    return (every ? _UTIL_ALL : _UTIL_ANY).get(input, checkUp(flwor, ctx)).comp(ctx, scp);
  }

  @Override
  public Bln item(final QueryContext ctx, final InputInfo ii)
      throws QueryException {
    throw Util.notexpected("Should have been optimized away.");
  }

  @Override
  public boolean uses(final Use u) {
    return u == Use.VAR || sat.uses(u);
  }

  @Override
  public boolean removable(final Var v) {
    for(final For f : fl) if(!f.removable(v)) return false;
    return sat.removable(v);
  }

  @Override
  public Expr remove(final Var v) {
    for(final For f : fl) f.remove(v);
    sat = sat.remove(v);
    return this;
  }

  @Override
  public void plan(final Serializer ser) throws IOException {
    ser.openElement(this, TYP, Token.token(every ? EVERY : SOME));
    for(final Expr f : fl) f.plan(ser);
    sat.plan(ser);
    ser.closeElement();
  }

  @Override
  public String toString() {
    final StringBuilder sb = new StringBuilder(every ? EVERY : SOME);
    for(final For f : fl) sb.append(' ').append(f);
    return sb.append(' ' + SATISFIES + ' ' + sat).toString();
  }

  @Override
  public boolean visitVars(final VarVisitor visitor) {
    if(!(visitor.visitAll(fl) && sat.visitVars(visitor))) return false;
    for(int i = fl.length; --i >= 0;) {
      final Var[] vars = fl[i].vars();
      for(int j = vars.length; --j >= 0;)
        if(!visitor.undeclared(vars[j])) return false;
    }
    return true;
  }
}
