package org.basex.gui;

import static org.basex.core.Text.*;

import java.awt.*;

import org.basex.core.*;
import org.basex.gui.layout.*;

/**
 * This is the status bar of the main window. It displays progress information
 * and includes a memory status.
 *
 * @author BaseX Team 2005-12, BSD License
 * @author Christian Gruen
 */
public final class GUIStatus extends BaseXPanel {
  /** Status text. */
  private final BaseXLabel label;

  /**
   * Constructor.
   * @param main reference to the main window
   */
  GUIStatus(final AGUI main) {
    super(main);
    BaseXLayout.setHeight(this, getFont().getSize() + 6);
    addMouseListener(this);
    addMouseMotionListener(this);

    layout(new BorderLayout(4, 0));
    label = new BaseXLabel(OK).border(0, 4, 0, 0);
    if (Prop.langright) {
      add(new BaseXMem(main, true), BorderLayout.WEST);
      add(label, BorderLayout.EAST);
    } else {
      add(new BaseXMem(main, true), BorderLayout.EAST);
      add(label, BorderLayout.CENTER);
    }
  }

  /**
   * Sets the status text.
   * @param txt the text to be set
   */
  public void setText(final String txt) {
    label.setText(txt);
    label.setForeground(Color.BLACK);
  }

  /**
   * Sets the status text.
   * @param txt the text to be set
   */
  public void setError(final String txt) {
    label.setText(txt);
    label.setForeground(GUIConstants.RED);
  }
}
