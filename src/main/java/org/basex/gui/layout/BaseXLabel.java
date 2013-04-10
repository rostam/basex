package org.basex.gui.layout;

import java.awt.*;

import javax.swing.*;
import javax.swing.border.*;

import org.basex.core.*;
import org.basex.gui.GUIConstants.Msg;

/**
 * Project specific Label implementation.
 *
 * @author BaseX Team 2005-12, BSD License
 * @author Christian Gruen
 */
public class BaseXLabel extends JLabel {
  
  public boolean rtlEx;
  
  
  /**
   * Constructor.
   */
  public BaseXLabel() {
    this("", false, false);
    //rtlEx = false;
    if (Prop.langright && !rtlEx) applyComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
  }

  /**
   * Constructor, specifying a label text.
   * @param txt label text
   */
  public BaseXLabel(final String txt) {
    this(txt, false, false);
    //rtlEx = false;
    if (Prop.langright && !rtlEx) applyComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
  }

  /**
   * Constructor, specifying a label text, vertical distance to the next
   * component, and a property for printing the label in bold.
   * @param txt label text
   * @param dist vertical distance to next component
   * @param bold bold flagRIGHT_TO_LEFT
   */
  public BaseXLabel(final String txt, final boolean dist, final boolean bold) {
    super(txt);
    if (Prop.langright && !rtlEx) applyComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
    if(dist) border(0, 0, 6, 0);
    setFont(getFont().deriveFont(bold ? Font.BOLD : Font.PLAIN));
  }

  public void SetRtlEx(final boolean rtl) {
    rtlEx = rtl;
  }
  
  public boolean GetRtlEx () {
    return rtlEx;
  }
  /**
   * Sets the label borders.
   * @param t top distance
   * @param l left distance
   * @param b bottom distance
   * @param r right distance
   * @return self reference
   */
  public BaseXLabel border(final int t, final int l, final int b, final int r) {
    setBorder(new EmptyBorder(t, l, b, r));
    return this;
  }

  /**
   * Sets the text color.
   * @param c color
   * @return self reference
   */
  public BaseXLabel color(final Color c) {
    setForeground(c);
    return this;
  }

  /**
   * Shows an text, preceded by a state icon.
   * If the text is {@code null}, no text and icon is shown.
   * @param text warning text
   * @param icon flag for displaying a warning or error icon
   * @return self reference
   */
  public BaseXLabel setText(final String text, final Msg icon) {
    System.out.println(text + " " + rtlEx);
    if (Prop.langright && rtlEx) 
      this.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);  
    setIcon(text == null ? null : icon.small);
    setText(text == null ? " " : text);
    return this;
  }

  /**
   * Chooses a large font.
   * @return self reference
   */
  public BaseXLabel large() {
    final Font f = getFont();
    setFont(new Font(f.getName(), Font.PLAIN, (int) f.getSize2D() + 7));
    return this;
  }

  @Override
  public void setEnabled(final boolean flag) {
    if(flag != isEnabled()) super.setEnabled(flag);
  }
}
