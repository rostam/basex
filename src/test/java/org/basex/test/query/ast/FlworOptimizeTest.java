package org.basex.test.query.ast;

import org.junit.Test;

/**
 * Tests for rewritings of FLWOR-expressions.
 *
 * @author BaseX Team 2005-12, BSD License
 * @author Leo Woerteler
 */
public final class FlworOptimizeTest extends QueryPlanTest {

  /* Let sliding. */

  /** Tests the relocation of a static let clause. */
  @Test public void moveTopTest() {
    check("let $seq := ('a', 'b', 'c') " +
        "for $i in 1 to count($seq) " +
        "for $j in $i + 1 to count($seq) " +
        "let $m := $seq[(count($seq) + 1) idiv 2] " +
        "return concat($i, $j, $m)",

        "12b 13b 23b",
        "every $for in //For satisfies //Let[Var/@name = '$m'] << $for"
    );
  }

  /** Tests the relocation of a let clause. */
  @Test public void moveMidTest() {
    check("let $seq := ('a', 'b', 'c') " +
        "for $i in 1 to count($seq) " +
        "for $j in $i + 1 to count($seq) " +
        "let $a := $seq[$i] " +
        "return concat($i, $j, $a)",

        "12a 13a 23b",
        "let $a := //Let[Var/@name = '$a'] return " +
          "//For[Var/@name = '$i'] << $a and $a << //For[Var/@name = '$j']"
    );
  }

  /** Tests the relocation of a let clause. */
  @Test public void dontMoveTest() {
    check("let $seq := ('a', 'b', 'c') " +
        "for $i in 1 to count($seq) " +
        "for $j in $i + 1 to count($seq) " +
        "let $b := $seq[$j] " +
        "return concat($i, $j, $b)",

        "12b 13c 23c",
        "every $for in //For satisfies $for << //Let[Var/@name = '$b']"
    );
  }

  /** Tests if let clauses using constructors are left alone. */
  @Test public void dontMoveCnsTest() {
    check("for $a in 1 to 2 " +
        "let $x := <x>x</x> " +
        "for $b in 3 to 4 " +
        "return ($x/text(), $b)",

        "x3x4x3x4",
        "//For[Var/@name = '$a'] << //Let and //Let << //For[Var/@name = '$b']"
    );
  }

  /** Tests if let clauses using non-deterministic functions are left alone. */
  @Test public void dontMoveNdtTest() {
    check("for $a in 1 to 2 " +
        "let $x := math:random() " +
        "for $b in 3 to 4 " +
        "return $b + floor($x)",

        "3 4 3 4",
        "//For[Var/@name = '$a'] << //Let and //Let << //For[Var/@name = '$b']"
    );
  }

  /* constant propagation */

  /** Tests if constant expressions in let clauses are inlined. */
  @Test public void constProp() {
    check("let $a := 1 * 21 " +
        "return sum( " +
        "  for $i in 1 to 2 " +
        "  return $a " +
        ")",

        "42",
        "count(//GFLWOR) lt 2 and empty(//Let)"
    );
  }

  /** Tests if constant function items in let clauses are inlined. */
  @Test public void constFuncProp() {
    check("let $f := function($x) { xs:string($x) } return $f(<x>foobar</x>)",
        "foobar",
        "exists(//FuncItem) and empty(//GFLWOR)"
    );
  }

  /** Tests if non-deterministic functions in let clauses are left alone. */
  @Test public void dontPropNdt() {
    check("let $x := math:random() return $x - $x",
        "0",
        "exists(//Let) and count(//Var[@name = '$x']) > 2"
    );
  }

  /** Tests if constants are inlined into oder-by expressions. */
  @Test public void seeThroughOrder() {
    check("for $a in (3, 2, 1) " +
        "for $b in (6, 5, 4) " +
        "count $i " +
        "let $e := 1 " +
        "stable order by " +
        "  $e empty least, " +
        "  $e descending empty least " +
        "return ($i, $a)",
        "1 3 2 3 3 3 4 2 5 2 6 2 7 1 8 1 9 1",
        "empty(//Let)"
    );
  }

  /* Remove let clauses with unused variables. */

  /** Tests if unused variables constructing nodes are removed. */
  @Test public void rmVar() {
    check("let $x := <x/>, $y := math:random() return $y < 1",
        "true",
        "empty(//Let[Var/@name = '$x'])"
    );
  }

  /** Tests if non-deterministic functions in let clauses are left alone. */
  @Test public void dontRmNdt() {
    check("let $x := math:random(), $y := math:random() return $y < 1",
        "true",
        "exists(//Let[Var/@name = '$x'])"
    );
  }

  /** Tests if non-deterministic functions in let clauses are left alone. */
  @Test public void rmFuncCall() {
    check("declare function local:foo() { for $i in 1 to 100000 return math:sqrt($i) };" +
        "let $x := local:foo() return 42",
        "42",
        "empty(//Let)"
    );
  }

  /** Tests if a variable is removed below a group and a where clause. */
  @Test public void rmConst() {
    check("for $x in 1 to 10 " +
        "let $key := $x mod 3, " +
        "    $foo := $key * 3 " +
        "group by $key " +
        "order by $key " +
        "return hof:const(string(text{$x}), $foo)",
        "3 6 9 1 4 7 10 2 5 8",
        "count(//Let) eq 1"
    );
  }
}
