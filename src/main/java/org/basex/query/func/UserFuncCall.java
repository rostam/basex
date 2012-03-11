package org.basex.query.func;

import static org.basex.query.QueryText.*;
import java.io.IOException;
import org.basex.io.serial.Serializer;
import org.basex.query.*;
import org.basex.query.expr.Arr;
import org.basex.query.expr.Expr;
import org.basex.query.item.QNm;
import org.basex.query.item.Value;
import org.basex.query.util.Var;
import org.basex.util.InputInfo;
import org.basex.util.Token;
import org.basex.util.TokenBuilder;

/**
 * Function call for user-defined functions.
 *
 * @author BaseX Team 2005-12, BSD License
 * @author Christian Gruen
 */
public abstract class UserFuncCall extends Arr {
  /**
   * A continuation that's thrown to free stack frames.
   * @author Leo Woerteler
   */
  final class Continuation extends RuntimeException {
    /** Arguments. */
    private final Value[] args;

    /**
     * Constructor.
     * @param arg arguments
     */
    Continuation(final Value[] arg) {
      args = arg;
    }

    /**
     * Getter for the continuation function.
     * @return the next function to call
     */
    UserFunc getFunc() {
      return func;
    }

    /**
     * Getter for the function arguments.
     * @return the next function call's arguments
     */
    Value[] getArgs() {
      return args;
    }

    @Override
    public synchronized Continuation fillInStackTrace() {
      // ignore this for efficiency reasons
      return this;
    }
  }

  /** Function name. */
  final QNm name;
  /** Function reference. */
  UserFunc func;

  /**
   * Function constructor.
   * @param ii input info
   * @param nm function name
   * @param arg arguments
   */
  UserFuncCall(final InputInfo ii, final QNm nm, final Expr... arg) {
    super(ii, arg);
    name = nm;
  }

  /**
   * Initializes the function call after all functions have been declared.
   * @param f function reference
   */
  public void init(final UserFunc f) {
    func = f;
  }

  /**
   * Getter for the called function.
   * @return user-defined function
   */
  final UserFunc func() {
    return func;
  }

  @Override
  public Expr comp(final QueryContext ctx, final VarScope scp) throws QueryException {
    // compile all arguments
    super.comp(ctx, scp);

    // inline if result and arguments are all values.
    // currently, only functions with values as
    // return expressions are supported; otherwise, recursive functions
    // might not be correctly evaluated
    func.comp(ctx, scp);
    if(func.expr.isValue() && allAreValues() && !func.uses(Use.NDT)) {
      // evaluate arguments to catch cast exceptions
      for(int a = 0; a < expr.length; ++a) ctx.set(func.args[a], (Value) expr[a], input);
      ctx.compInfo(OPTINLINE, func.name.string());
      return func.value(ctx);
    }
    // user-defined functions are not pre-evaluated to avoid various issues
    // with recursive functions
    type = func.type();
    return this;
  }

  /**
   * Adds the given arguments to the variable stack.
   * @param ctx query context
   * @param ii input info
   * @param vars formal parameters
   * @param vals values to add
   * @return old stack frame
   * @throws QueryException if the arguments can't be bound
   */
  static Value[] addArgs(final QueryContext ctx, final InputInfo ii, final Var[] vars,
      final Value[] vals) throws QueryException {
    // move variables to stack
    final Value[] old = ctx.pushStackFrame(vars.length);
    for(int i = 0; i < vars.length; i++) ctx.set(vars[i], vals[i], ii);
    return old;
  }

  /**
   * Evaluates all function arguments.
   * @param ctx query context
   * @return argument values
   * @throws QueryException query exception
   */
  Value[] args(final QueryContext ctx) throws QueryException {
    final int al = expr.length;
    final Value[] args = new Value[al];
    // evaluate arguments
    for(int a = 0; a < al; ++a) args[a] = expr[a].value(ctx);
    return args;
  }

  @Override
  public boolean uses(final Use u) {
    return u == Use.UPD ? func.updating : super.uses(u);
  }

  @Override
  public void plan(final Serializer ser) throws IOException {
    ser.openElement(this, NAM, Token.token(toString()));
    for(final Expr e : expr) e.plan(ser);
    ser.closeElement();
  }

  @Override
  public String description() {
    return FUNC;
  }

  @Override
  public String toString() {
    return new TokenBuilder(name.string()).add(PAR1).add(
        toString(SEP)).add(PAR2).toString();
  }
}
