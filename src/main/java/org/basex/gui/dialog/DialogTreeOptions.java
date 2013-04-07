package org.basex.gui.dialog;

import static org.basex.core.Text.*;

import java.awt.*;

import org.basex.core.*;
import org.basex.gui.*;
import org.basex.gui.layout.*;

/**
 * Dialog window for specifying the TreeMap layout.
 *
 * @author BaseX Team 2005-12, BSD License
 * @author Wolfgang Miller
 */
public final class DialogTreeOptions extends BaseXDialog {
  /** Slim rectangles to text length. */
  private final BaseXCheckBox slim;
  /** Show attributes. */
  private final BaseXCheckBox atts;

  /**
   * Default constructor.
   * @param main reference to the main window
   */
  public DialogTreeOptions(final GUI main) {
    super(main, TREE_OPTIONS, false);

    final BaseXBack p;
    if (Prop.langright) p = new BaseXBack(new RTLTableLayout(2, 1, 0, 8));
    else p = new BaseXBack(new TableLayout(2, 1, 0, 8));
    final GUIProp gprop = gui.gprop;

    // create checkbox
    slim = new BaseXCheckBox(ADJUST_NODES, gprop.is(GUIProp.TREESLIMS), this);
    p.add(slim);

    // create checkbox
    atts = new BaseXCheckBox(SHOW_ATTS, gprop.is(GUIProp.TREEATTS), this);
    p.add(atts);

    set(p, BorderLayout.CENTER);
    finish(gprop.nums(GUIProp.MAPLAYOUTLOC));
  }

  @Override
  public void action(final Object cmp) {
    final GUIProp gprop = gui.gprop;
    gprop.set(GUIProp.TREESLIMS, slim.isSelected());
    gprop.set(GUIProp.TREEATTS, atts.isSelected());
    gui.notify.layout();
  }
}
