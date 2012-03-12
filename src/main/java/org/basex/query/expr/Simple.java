package org.basex.query.expr;

import java.io.IOException;

import org.basex.io.serial.Serializer;
import org.basex.query.*;
import org.basex.query.util.*;
import org.basex.query.var.*;
import org.basex.util.InputInfo;

/**
 * Simple expression without arguments.
 *
 * @author BaseX Team 2005-12, BSD License
 * @author Christian Gruen
 */
public abstract class Simple extends ParseExpr {
  /**
   * Constructor.
   * @param ii input info
   */
  protected Simple(final InputInfo ii) {
    super(ii);
  }

  @Override
  public Expr comp(final QueryContext ctx, final VarScope scp) {
    return this;
  }

  @Override
  public boolean uses(final Use u) {
    return false;
  }

  @Override
  public boolean removable(final Var v) {
    return true;
  }

  @Override
  public Expr remove(final Var v) {
    return this;
  }

  @Override
  public void plan(final Serializer ser) throws IOException {
    ser.emptyElement(this);
  }

  @Override
  public boolean visitVars(final VarVisitor visitor) {
    return true;
  }
}
