package org.basex.query.path;

import static org.basex.query.QueryText.*;
import static org.basex.query.util.Err.*;

import java.util.*;

import org.basex.data.*;
import org.basex.index.path.*;
import org.basex.index.stats.*;
import org.basex.query.*;
import org.basex.query.expr.*;
import org.basex.query.iter.*;
import org.basex.query.path.Test.Mode;
import org.basex.query.util.*;
import org.basex.query.value.*;
import org.basex.query.value.item.*;
import org.basex.query.value.node.*;
import org.basex.query.value.seq.*;
import org.basex.query.value.type.*;
import org.basex.util.*;

/**
 * Axis path expression.
 *
 * @author BaseX Team 2005-12, BSD License
 * @author Christian Gruen
 */
public class AxisPath extends Path {
  /** Flag for result caching. */
  private boolean cache;
  /** Cached result. */
  private NodeSeqBuilder citer;
  /** Last visited item. */
  private Value lvalue;

  /**
   * Constructor.
   * @param ii input info
   * @param r root expression; can be a {@code null} reference
   * @param s axis steps
   */
  AxisPath(final InputInfo ii, final Expr r, final Expr... s) {
    super(ii, r, s);
  }

  /**
   * If possible, converts this path expression to a path iterator.
   * @param ctx query context
   * @return resulting operator
   */
  final AxisPath finish(final QueryContext ctx) {
    // evaluate number of results
    size = size(ctx);
    type = SeqType.get(steps[steps.length - 1].type().type, size);
    return useIterator() ? new IterPath(info, root, steps, type, size) : this;
  }

  /**
   * Checks if the path can be rewritten for iterative evaluation.
   * @return resulting operator
   */
  private boolean useIterator() {
    if(root == null || root.uses(Use.VAR) || !root.iterable()) return false;

    final int sl = steps.length;
    for(int s = 0; s < sl; ++s) {
      switch(step(s).axis) {
        // reverse axes - don't iterate
        case ANC: case ANCORSELF: case PREC: case PRECSIBL:
          return false;
        // multiple, unsorted results - only iterate at last step,
        // or if last step uses attribute axis
        case DESC: case DESCORSELF: case FOLL: case FOLLSIBL:
          return s + 1 == sl || s + 2 == sl && step(s + 1).axis == Axis.ATTR;
        // allow iteration for CHILD, ATTR, PARENT and SELF
        default:
      }
    }
    return true;
  }

  @Override
  protected final Expr compilePath(final QueryContext ctx) throws QueryException {
    // merge two axis paths
    if(root instanceof AxisPath) {
      Expr[] st = ((AxisPath) root).steps;
      root = ((AxisPath) root).root;
      for(final Expr s : steps) st = Array.add(st, s);
      steps = st;
      // refresh root context
      ctx.compInfo(OPTMERGE);
      ctx.value = root(ctx);
    }

    final AxisStep s = voidStep(steps);
    if(s != null) COMPSELF.thrw(info, s);

    for(int i = 0; i != steps.length; ++i) {
      final Expr e = steps[i].compile(ctx);
      if(!(e instanceof AxisStep)) return e;
      steps[i] = e;
    }
    optSteps(ctx);

    // retrieve data reference
    final Data data = ctx.data();
    if(data != null && ctx.value.type == NodeType.DOC) {
      // check index access
      Expr e = index(ctx, data);
      // check children path rewriting
      if(e == this) e = children(ctx, data);
      // return optimized expression
      if(e != this) return e.compile(ctx);
    }

    // analyze if result set can be cached - no predicates/variables...
    cache = root != null && !uses(Use.VAR);

    // if applicable, use iterative evaluation
    final Path path = finish(ctx);

    // heuristics: wrap with filter expression if only one result is expected
    return size() != 1 ? path :
      new Filter(info, this, Pos.get(1, size(), info)).comp2(ctx);
  }

  /**
   * If possible, returns an expression which accesses the index.
   * Otherwise, returns the original expression.
   * @param ctx query context
   * @param data data reference
   * @return resulting expression
   * @throws QueryException query exception
   */
  private Expr index(final QueryContext ctx, final Data data) throws QueryException {
    // disallow relative paths and numeric predicates
    if(root == null || uses(Use.POS)) return this;

    // cache index access costs
    IndexContext ics = null;
    // cheapest predicate and step
    int pmin = 0;
    int smin = 0;

    // check if path can be converted to an index access
    for(int s = 0; s < steps.length; ++s) {
      // find cheapest index access
      final AxisStep stp = step(s);
      if(!stp.axis.down) break;

      // check if resulting index path will be duplicate free
      final boolean i = pathNodes(data, s) != null;

      // choose cheapest index access
      for(int p = 0; p < stp.preds.length; ++p) {
        final IndexContext ic = new IndexContext(ctx, data, stp, i);
        if(!stp.preds[p].indexAccessible(ic)) continue;

        if(ic.costs() == 0) {
          if(ic.not) {
            // not operator... accept all results
            stp.preds[p] = Bln.TRUE;
            continue;
          }
          // no results...
          ctx.compInfo(OPTNOINDEX, this);
          return Empty.SEQ;
        }
        if(ics == null || ics.costs() > ic.costs()) {
          ics = ic;
          pmin = p;
          smin = s;
        }
      }
    }

    // skip if no index access is possible, or if it is too expensive
    if(ics == null || ics.costs() > data.meta.size) return this;

    // replace expressions for index access
    final AxisStep stp = step(smin);
    final Expr ie = stp.preds[pmin].indexEquivalent(ics);

    if(ics.seq) {
      // sequential evaluation; do not invert path
      stp.preds[pmin] = ie;
    } else {
      // inverted path, which will be represented as predicate
      AxisStep[] invSteps = {};

      // collect remaining predicates
      final Expr[] newPreds = new Expr[stp.preds.length - 1];
      int c = 0;
      for(int p = 0; p != stp.preds.length; ++p) {
        if(p != pmin) newPreds[c++] = stp.preds[p];
      }

      // check if path before index step needs to be inverted and traversed
      final Test test = InvDocTest.get(ctx, data);
      boolean inv = true;
      if(test == Test.DOC && data.meta.uptodate) {
        int j = 0;
        for(; j <= smin; ++j) {
          final AxisStep s = axisStep(j);
          // step must use child axis and name test, and have no predicates
          if(s == null || s.test.mode != Mode.NAME || s.axis != Axis.CHILD ||
              j != smin && s.preds.length > 0) break;

          // support only unique paths with nodes on the correct level
          final int name = data.tagindex.id(s.test.name.local());
          final ArrayList<PathNode> pn = data.paths.desc(name, Data.ELEM);
          if(pn.size() != 1 || pn.get(0).level() != j + 1) break;
        }
        inv = j <= smin;
      }

      // invert path before index step
      if(inv) {
        for(int j = smin; j >= 0; --j) {
          final Axis ax = step(j).axis.invert();
          if(ax == null) break;
          if(j != 0) {
            final AxisStep prev = step(j - 1);
            invSteps = Array.add(invSteps, AxisStep.get(info, ax, prev.test, prev.preds));
          } else {
            // add document test for collections and axes other than ancestors
            if(test != Test.DOC || ax != Axis.ANC && ax != Axis.ANCORSELF)
              invSteps = Array.add(invSteps, AxisStep.get(info, ax, test));
          }
        }
      }

      // create resulting expression
      final AxisPath result;
      final boolean simple = invSteps.length == 0 && newPreds.length == 0;
      if(ie instanceof AxisPath) {
        result = (AxisPath) ie;
      } else if(smin + 1 < steps.length || !simple) {
        result = simple ? new AxisPath(info, ie) :
          new AxisPath(info, ie, AxisStep.get(info, Axis.SELF, Test.NOD));
      } else {
        return ie;
      }

      // add remaining predicates to last step
      final int ls = result.steps.length - 1;
      if(ls >= 0) {
        result.steps[ls] = result.step(ls).addPreds(newPreds);
        // add inverted path as predicate to last step
        if(invSteps.length != 0) result.steps[ls] =
            result.step(ls).addPreds(Path.get(info, null, invSteps));
      }

      // add remaining steps
      for(int s = smin + 1; s < steps.length; ++s) {
        result.steps = Array.add(result.steps, steps[s]);
      }
      return result;
    }
    return this;
  }

  @Override
  public Iter iter(final QueryContext ctx) throws QueryException {
    final Value cv = ctx.value;
    final long cs = ctx.size;
    final long cp = ctx.pos;
    final Value r = root != null ? ctx.value(root) : cv;

    try {
      /* cache values if:
       * - caching is desirable
       * - the code is called for the first time
       * - the value has changed and the underlying node is not the same
       */
      if(!cache || citer == null || lvalue != r && !(r instanceof ANode &&
          lvalue instanceof ANode && ((ANode) lvalue).is((ANode) r))) {
        lvalue = r;
        citer = new NodeSeqBuilder().check();
        if(r != null) {
          final Iter ir = ctx.iter(r);
          for(Item it; (it = ir.next()) != null;) {
            ctx.value = it;
            iter(0, citer, ctx);
          }
        } else {
          ctx.value = null;
          iter(0, citer, ctx);
        }
        citer.sort();
      } else {
        citer.reset();
      }
      return citer;
    } finally {
      ctx.value = cv;
      ctx.size = cs;
      ctx.pos = cp;
    }
  }

  /**
   * Recursive step iterator.
   * @param l current step
   * @param nc node cache
   * @param ctx query context
   * @throws QueryException query exception
   */
  private void iter(final int l, final NodeSeqBuilder nc, final QueryContext ctx)
      throws QueryException {

    // cast is safe (steps will always return a {@link NodIter} instance
    final NodeIter ni = (NodeIter) ctx.iter(steps[l]);
    final boolean more = l + 1 != steps.length;
    for(ANode node; (node = ni.next()) != null;) {
      if(more) {
        ctx.value = node;
        iter(l + 1, nc, ctx);
      } else {
        ctx.checkStop();
        nc.add(node);
      }
    }
  }

  /**
   * Inverts a location path.
   * @param r new root node
   * @param curr current location step
   * @return inverted path
   */
  public final AxisPath invertPath(final Expr r, final AxisStep curr) {
    // hold the steps to the end of the inverted path
    int s = steps.length;
    final Expr[] e = new Expr[s--];
    // add predicates of last step to new root node
    final Expr rt = step(s).preds.length != 0 ?
        new Filter(info, r, step(s).preds) : r;

    // add inverted steps in a backward manner
    int c = 0;
    while(--s >= 0) {
      e[c++] = AxisStep.get(info, step(s + 1).axis.invert(), step(s).test, step(s).preds);
    }
    e[c] = AxisStep.get(info, step(s + 1).axis.invert(), curr.test);
    return new AxisPath(info, rt, e);
  }

  @Override
  public final Expr addText(final QueryContext ctx) {
    final AxisStep s = step(steps.length - 1);

    if(s.preds.length != 0 || !s.axis.down || s.test.type == NodeType.ATT ||
        s.test.mode != Mode.NAME && s.test.mode != Mode.STD) return this;

    final Data data = ctx.data();
    if(data == null || !data.meta.uptodate) return this;

    final Stats stats = data.tagindex.stat(data.tagindex.id(s.test.name.local()));
    if(stats != null && stats.isLeaf()) {
      steps = Array.add(steps, AxisStep.get(info, Axis.CHILD, Test.TXT));
      ctx.compInfo(OPTTEXT, this);
    }
    return this;
  }

  /**
   * Returns the specified axis step.
   * @param i index
   * @return step
   */
  public final AxisStep step(final int i) {
    return (AxisStep) steps[i];
  }

  @Override
  public final Path copy() {
    final Expr[] stps = new Expr[steps.length];
    for(int s = 0; s < steps.length; ++s) stps[s] = AxisStep.get(step(s));
    return new AxisPath(info, root, stps);
  }

  /**
   * Returns the path nodes that will result from this path.
   * @param ctx query context
   * @return path nodes, or {@code null} if nodes cannot be evaluated
   */
  public ArrayList<PathNode> nodes(final QueryContext ctx) {
    final Value rt = root(ctx);
    final Data data = rt != null && rt.type == NodeType.DOC ? rt.data() : null;
    if(data == null || !data.meta.uptodate) return null;

    ArrayList<PathNode> nodes = data.paths.root();
    for(int s = 0; s < steps.length; s++) {
      final AxisStep curr = axisStep(s);
      if(curr == null) return null;
      nodes = curr.nodes(nodes, data);
      if(nodes == null) return null;
    }
    return nodes;
  }

  @Override
  public final int count(final Var v) {
    int c = 0;
    for(final Expr s : steps) c += s.count(v);
    return c + super.count(v);
  }

  @Override
  public final boolean removable(final Var v) {
    for(final Expr s : steps) if(!s.removable(v)) return false;
    return super.removable(v);
  }

  @Override
  public final Expr remove(final Var v) {
    for(int s = 0; s != steps.length; ++s) steps[s].remove(v);
    return super.remove(v);
  }

  @Override
  public final boolean iterable() {
    return true;
  }

  @Override
  public final boolean sameAs(final Expr cmp) {
    if(!(cmp instanceof AxisPath)) return false;
    final AxisPath ap = (AxisPath) cmp;
    if((root == null || ap.root == null) && root != ap.root ||
        steps.length != ap.steps.length ||
        root != null && !root.sameAs(ap.root)) return false;

    for(int s = 0; s < steps.length; ++s) {
      if(!steps[s].sameAs(ap.steps[s])) return false;
    }
    return true;
  }
}
