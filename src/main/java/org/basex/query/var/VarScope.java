package org.basex.query.var;

import static org.basex.util.Token.*;

import java.util.*;

import org.basex.query.*;
import org.basex.query.expr.*;
import org.basex.query.func.*;
import org.basex.query.item.*;
import org.basex.query.util.*;
import org.basex.query.var.Var.*;
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
  private final HashSet<Var> vars = new HashSet<Var>();

  /** This scope's closure. */
  private final Map<Var, VarRef> closure = new HashMap<Var, VarRef>();

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
  private Var add(final Var var) {
    var.slot = vars.size();
    if(!vars.add(var)) throw Util.notexpected(var);
    current.push(var);
    return var;
  }

  /**
   * Resolves a variable and adds it to all enclosing scopes.
   * @param name variable name
   * @param qp parser
   * @param ctx query context
   * @param ii input info
   * @param err error to be thrown if the variable doesn't exist
   * @return variable reference
   * @throws QueryException if the variable can't be found
   */
  public Var resolve(final QNm name, final QueryParser qp, final QueryContext ctx,
      final InputInfo ii, final Err err) throws QueryException {
    final Var v = current.get(name);
    if(v != null) return v;

    if(parent != null) {
      final Var nonLocal = parent.resolve(name, qp, ctx, ii, err);
      if(nonLocal.kind == VarKind.GLOBAL) return nonLocal;

      // a variable in the closure
      final Var local = new Var(ctx, name, null);
      local.refineType(nonLocal.type());
      add(local);
      closure.put(local, new VarRef(ii, nonLocal));
      return local;
    }

    // global variable
    GlobalVar global = ctx.globals.get(name);
    if(global == null) global = Variable.get(name, ctx);
    if(global == null && err != null) throw qp.error(err, '$' + string(name.string()));
    return global.var;
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
  public VarScope child() {
    return new VarScope(this);
  }

  /**
   * Parent scope of this scope.
   * @return parent
   */
  public VarScope parent() {
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
   * @param param function parameter flag
   * @return the variable
   */
  public Var newLocal(final QueryContext ctx, final QNm name, final SeqType typ,
      final boolean param) {
    return add(new Var(ctx, name, typ, param ? VarKind.FUNC_PARAM : VarKind.LOCAL));
  }

  /**
   * Get the closure of this scope.
   * @return mapping from non-local to local variables
   */
  public Map<Var, VarRef> closure() {
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
   * Enters this scope.
   * @param ctx query context
   * @return old stack frame
   */
  public Value[] enter(final QueryContext ctx) {
    final Value[] old = ctx.stackFrame;
    ctx.stackFrame = new Value[vars.size()];
    return old;
  }

  /**
   * Exits this scope.
   * @param ctx query context
   * @param old stack frame of the enclosing scope, or {@code null}
   */
  public void exit(final QueryContext ctx, final Value[] old) {
    ctx.stackFrame = old;
  }

  /**
   * Deletes all unused variables from this scope and assigns stack slots.
   * This method should be run after compiling the scope.
   * @param expr the scope
   */
  public void cleanUp(final Scope expr) {
    final BitSet declared = new BitSet();
    final int[] counter = new int[1];
    expr.visit(new VarVisitor() {
      @Override
      public boolean declared(final Var var) {
        declared.set(var.id);
        var.slot = counter[0]++;
        return true;
      }
    });

    // purge all unused variables
    final Iterator<Var> iter = vars.iterator();
    while(iter.hasNext()) if(!declared.get(iter.next().id)) iter.remove();
  }
}
