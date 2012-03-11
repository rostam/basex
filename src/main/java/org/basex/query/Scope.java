package org.basex.query;

import org.basex.query.util.*;

/**
 * Interface for all expressions defining a new variable scope.
 *
 * @author Leo Woerteler
 */
public interface Scope {
  /**
   * Traverses this scope with the given {@link VarVisitor}.
   * @param visitor visitor
   * @return continue flag
   */
  boolean visit(final VarVisitor visitor);
}
