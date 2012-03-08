package org.basex.query;

import static org.basex.query.util.Err.*;
import org.basex.data.ExprInfo;
import org.basex.io.serial.*;

import java.io.*;
import java.util.*;
import java.util.Map.Entry;
import org.basex.query.item.QNm;
import org.basex.query.expr.Expr;
import org.basex.query.util.*;
import org.basex.util.InputInfo;


/**
 * Container of global variables of a module.
 *
 * @author BaseX Team 2005-12, BSD License
 * @author Leo Woerteler
 */
public final class Globals extends ExprInfo {
  /** The global variables. */
  private final HashMap<Var, StaticVar> globals = new HashMap<Var, StaticVar>();

  /**
   * Looks for a variable with the given name in the globally defined variables.
   * @param name variable name
   * @return declaration if found, {@null} otherwise
   */
  public StaticVar get(final QNm name) {
    for(final Entry<Var, StaticVar> e : globals.entrySet())
      if(e.getKey().name.eq(name)) return e.getValue();
    return null;
  }

  /**
   * Sets the given global variable.
   * @param ctx query context
   * @param ii input info
   * @param v variable
   * @param a annotations
   * @param e expression, possibly {@code null}
   * @param decl declaration flag
   * @return static variable
   * @throws QueryException query exception
   */
  public StaticVar set(final QueryContext ctx, final InputInfo ii, final Var v,
      final Ann a, final Expr e, final boolean decl) throws QueryException {
    final StaticVar var = globals.get(v);
    if(var != null) {
      if(decl && var.declared) throw VARDEFINE.thrw(ii, v);
      var.declared = decl;
      if(a != null) {
        for(int i = a.size(); --i >= 0;)
          var.ann.add(a.names[i], a.values[i]);
      }
      var.var.refineType(v.type());
      if(e != null) var.bind(e, ctx);

      return var;
    }

    // new variable
    final StaticVar nvar = new StaticVar(ii, v, a, e);
    nvar.declared = decl;
    globals.put(v, nvar);
    return nvar;
  }

  /**
   * Checks if none of the variables contains an updating expression.
   * @throws QueryException query exception
   */
  public void checkUp() throws QueryException {
    for(final StaticVar var : globals.values()) var.checkUp();
  }

  /**
   * Number of declared global variables.
   * @return number of variables
   */
  public int size() {
    return globals.size();
  }

  @Override
  public void plan(final Serializer ser) throws IOException {
    ser.openElement(this);
    for(final StaticVar v : globals.values()) v.plan(ser);
    ser.closeElement();
  }

  @Override
  public String toString() {
    final StringBuilder sb = new StringBuilder();
    for(final StaticVar v : globals.values()) sb.append(v);
    return sb.toString();
  }
}
