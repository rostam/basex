package org.basex.query.gflwor;

import java.io.*;
import java.util.*;

import org.basex.io.serial.*;
import org.basex.query.*;
import org.basex.query.expr.*;
import org.basex.query.item.Empty;
import org.basex.query.item.Item;
import org.basex.query.iter.Iter;
import org.basex.query.util.*;
import org.basex.query.var.*;
import org.basex.util.*;

/**
 * General FLWOR expression.
 *
 * @author BaseX Team 2005-12, BSD License
 * @author Leo Woerteler
 */
public class GFLWOR extends ParseExpr {
  /** Return expression. */
  Expr ret;
  /** FLWOR clauses. */
  private final ArrayList<Clause> clauses;
  /** XQuery 3.0 flag. */
  private boolean xq30;

  /**
   * Constructor.
   * @param ii input info
   * @param cls FLWOR clauses
   * @param rt return expression
   */
  public GFLWOR(final InputInfo ii, final ArrayList<Clause> cls, final Expr rt) {
    super(ii);
    clauses = cls;
    ret = rt;
  }

  @Override
  public Iter iter(final QueryContext ctx) throws QueryException {
    Eval e = start();
    for(final Clause cls : clauses) e = cls.eval(e);
    final Eval ev = e;

    return new Iter() {
      /** Return iterator. */
      private Iter sub = Empty.ITER;
      /** If the iterator has been emptied. */
      private boolean drained;
      @Override
      public Item next() throws QueryException {
        if(drained) return null;
        while(true) {
          final Item it = sub.next();
          if(it != null) return it;
          if(!ev.next(ctx)) {
            drained = true;
            return null;
          }
          sub = ret.iter(ctx);
        }
      }
    };
  }

  /**
   * Start evaluator, doing nothing, once.
   * @return evaluator
   */
  private Eval start() {
    return new Eval() {
      /** First-evaluation flag. */
      private boolean first = true;
      @Override
      public boolean next(final QueryContext ctx) {
        if(!first) return false;
        first = false;
        return true;
      }
    };
  }

  @Override
  public Expr comp(final QueryContext ctx, final VarScope scp) throws QueryException {
    for(final Clause cl : clauses) cl.comp(ctx, scp);
    ret.comp(ctx, scp);
    // [LW] optimizations
    return this;
  }

  @Override
  public boolean uses(final Use u) {
    if(u == Use.VAR || u == Use.X30 && xq30) return true;
    for(final Clause cls : clauses) if(cls.uses(u)) return true;
    return ret.uses(u);
  }

  @Override
  public boolean removable(final Var v) {
    for(final Clause cl : clauses) if(!cl.removable(v)) return false;
    return ret.removable(v);
  }

  @Override
  public Expr remove(final Var v) {
    for(final Clause cl : clauses) cl.remove(v);
    return ret.remove(v);
  }

  @Override
  public boolean visitVars(final VarVisitor visitor) {
    for(final Clause cl : clauses) if(!cl.visitVars(visitor)) return false;
    if(!ret.visitVars(visitor)) return false;
    for(int i = clauses.size(); --i >= 0;)
      if(!clauses.get(i).undeclare(visitor)) return false;
    return true;
  }

  @Override
  public void plan(final Serializer ser) throws IOException {
    ser.openElement(this);
    for(final Clause cl : clauses) cl.plan(ser);
    ret.plan(ser);
    ser.closeElement();
  }

  @Override
  public String toString() {
    final StringBuilder sb = new StringBuilder();
    for(final Clause cl : clauses) sb.append(cl).append(' ');
    return sb.append(QueryText.RETURN).append(' ').append(ret).toString();
  }

  /**
   * Evaluator for FLWOR clauses.
   *
   * @author BaseX Team 2005-12, BSD License
   * @author Leo Woerteler
   */
  interface Eval {
    /**
     * Makes the next evaluation step if available. This method is guaranteed
     * to not be called again if it has once returned {@code false}.
     * @param ctx query context
     * @return {@code true} if step was made, {@code false} if no more
     * results exist
     * @throws QueryException evaluation exception
     */
    boolean next(final QueryContext ctx) throws QueryException;
  }

  /**
   * A FLWOR clause.
   *
   * @author BaseX Team 2005-12, BSD License
   * @author Leo Woerteler
   */
  public abstract static class Clause extends ParseExpr {
    /** All variables declared in this clause. */
    private Var[] vars;
    /**
     * Constructor.
     * @param ii input info
     * @param vs declared variables
     */
    protected Clause(final InputInfo ii, final Var... vs) {
      super(ii);
      vars = vs;
    }

    /**
     * Evaluates the clause.
     * @param sub wrapped evaluator
     * @return evaluator
     */
    abstract Eval eval(final Eval sub);

    @Override
    public abstract Clause comp(QueryContext ctx, final VarScope scp)
        throws QueryException;

    /**
     * Undeclares all declared variables.
     * @param visitor variable visitor
     * @return continue
     */
    final boolean undeclare(final VarVisitor visitor) {
      for(int i = vars.length; --i >= 0;) if(!visitor.undeclared(vars[i])) return false;
      return true;
    }

    /**
     * All declared variables of this clause.
     * @return declared variables
     */
    public final Var[] vars() {
      return vars;
    }

    /**
     * Checks if the given variable is declared by this clause.
     * @param v variable
     * @return {code true} if the variable was declared here, {@code false} otherwise
     */
    public final boolean declares(final Var v) {
      for(final Var decl : vars) if(v.is(decl)) return true;
      return false;
    }
  }
}
