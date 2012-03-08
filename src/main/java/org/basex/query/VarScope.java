package org.basex.query;

import static org.basex.util.Token.*;

import java.util.*;
import org.basex.query.func.*;
import org.basex.query.item.*;
import org.basex.query.util.*;
import org.basex.query.util.Var.VarKind;
import org.basex.util.*;

/**
 * The scope of variables, either the query, a use-defined or an inline function.
 *
 * @author BaseX Team 2005-12, BSD License
 * @author Leo Woerteler
 */
public final class VarScope {
  /** stack of currently accessible variables. */
  private final VarStack current = new VarStack();
  /** Local variables in this scope. */
  private final ArrayList<Var> vars = new ArrayList<Var>();

  /** This scope's closure. */
  private final Map<Var, Var> closure = new HashMap<Var, Var>();

  /** This scope's parent scope, used for looking up non-local variables. */
  private final VarScope parent;

  /** Constructor for a top-level module. */
  public VarScope() {
    this(null);
  }

  /**
   * Constructor.
   * @param par parent scope
   */
  private VarScope(final VarScope par) {
    parent = par;
  }

  /**
   * Adds a variable to this scope.
   * @param var variable to be added
   * @return the variable (for convenience)
   */
  Var add(final Var var) {
    vars.add(var);
    current.add(var);
    return var;
  }

  /**
   * Resolves a variable and adds it to all enclosing scopes.
   * @param name variable name
   * @param qp parser
   * @param ii input info
   * @param err error to be thrown if the variable doesn't exist
   * @return variable reference
   * @throws QueryException if the variable can't be found
   */
  Var resolve(final QNm name, final QueryParser qp, final InputInfo ii, final Err err)
      throws QueryException {
    final Var v = current.get(name);
    if(v != null) return v;

    if(parent != null) {
      final Var nonLocal = parent.resolve(name, qp, ii, err);
      if(nonLocal.kind == VarKind.GLOBAL) return nonLocal;

      // a variable in the closure
      final Var local = new Var(qp.ctx, name, null);
      local.refineType(nonLocal.type());
      add(local);
      closure.put(local, nonLocal);
      return local;
    }

    // global variable
    Var global = qp.ctx.globals.get(name).var;
    if(global == null) global = Variable.get(name, qp.ctx);
    if(global == null && err != null) throw qp.error(err, '$' + string(name.string()));
    return global;
  }

  /**
   * Opens a new sub-scope inside this scope. The returned marker has to be supplied to
   * the corresponding call to {@link VarScope#close(int)} in order to mark the variables
   * as inaccessible.
   * @return marker for the current bindings
   */
  public int open() {
    return current.size;
  }

  /**
   * Closes the sub-scope and marks all contained variables as inaccessible.
   * @param marker marker for the start of the sub-scope
   */
  public void close(final int marker) {
    current.size = marker;
  }

  /**
   * Get a sub-scope of this scope.
   * @return sub-scope
   */
  VarScope child() {
    return new VarScope(this);
  }

  /**
   * Parent scope of this scope.
   * @return parent
   */
  VarScope parent() {
    return parent;
  }

  /**
   * Creates a variable with a unique, non-clashing variable name.
   * @param ctx context for variable ID
   * @param type type
   * @param kind kind of the variable
   * @return variable
   */
  public Var uniqueVar(final QueryContext ctx, final SeqType type, final VarKind kind) {
    return add(new Var(ctx, new QNm(token(ctx.varIDs)), type, kind));
  }

  /**
   * Creates a new local variable in this scope.
   * @param ctx query context
   * @param name variable name
   * @param typ type of the variable
   * @return the variable
   */
  public Var newLocal(final QueryContext ctx, final QNm name, final SeqType typ) {
    return add(new Var(ctx, name, typ, VarKind.LOCAL));
  }

  /**
   * All local variables declared in this scope.
   * @return variable array
   */
  public Var[] stackVars() {
    return vars.toArray(new Var[vars.size()]);
  }

  /**
   * Get the closure of this scope.
   * @return mapping from non-local to local variables
   */
  public Map<Var, Var> closure() {
    return closure;
  }

  /**
   * Gets all local variables in this scope.
   * @return array containing all local variables
   */
  public Var[] locals() {
    return vars.toArray(new Var[vars.size()]);
  }

  /**
   * Size of the stack needed for this scope.
   * @return maximum number of variables on the stack at the same time.
   */
  public int stackSize() {
    // TODO Auto-generated method stub
    return -1;
  }
}
