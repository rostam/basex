package org.basex.gui.layout;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.border.*;

import org.basex.core.*;

/**
 * Project specific RadioButton implementation.
 *
 * @author BaseX Team 2005-12, BSD License
 * @author Christian Gruen
 */
public final class BaseXRadio extends JRadioButton {
  /**
   * Default constructor.
   * @param label button title
   * @param sel initial selection state
   * @param win parent window
   */
  public BaseXRadio(final String label, final boolean sel, final Window win) {
    super(label, sel);
    setOpaque(false);
    setBorder(new EmptyBorder(0, 0, 0, 16));
    BaseXLayout.addInteraction(this, win);
    if(Prop.langright) this.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
    if(!(win instanceof BaseXDialog)) return;
    addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(final ActionEvent e) {
        ((BaseXDialog) win).action(e.getSource());
      }
    });
  }
}
