package org.basex.gui;

import java.awt.*;

import javax.swing.*;

import org.basex.core.*;
import org.basex.gui.layout.*;

/**
 * This is the toolbar of the main window.
 * The toolbar contents are defined in {@link GUIConstants#TOOLBAR}.
 *
 * @author BaseX Team 2005-12, BSD License
 * @author Christian Gruen
 */
public final class GUIToolBar extends JToolBar {
  /** Toolbar commands. */
  private final GUICommand[] cmd;
  /** Reference to main window. */
  private final GUI gui;

  /**
   * Default constructor.
   * @param tb toolbar commands
   * @param main reference to the main window
   */
  public GUIToolBar(final GUICommand[] tb, final GUI main) {
    setFloatable(false);
    cmd = tb;
    gui = main;
    if(Prop.langright)
      setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);

    for(final GUICommand c : cmd) {
      if(c == null) {
        addSeparator();
      } else {
        final BaseXButton button = BaseXButton.command(c, gui);
        button.setFocusable(false);
        add(button);
      }
    }
  }

  /**
   * Refreshes the buttons.
   */
  void refresh() {
    for(int b = 0; b < cmd.length; ++b) {
      if(cmd[b] != null) cmd[b].refresh(gui, (BaseXButton) getComponent(b));
    }
  }
}
