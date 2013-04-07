package org.basex.gui.dialog;

import static org.basex.core.Text.*;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.nio.charset.*;
import java.util.*;

import org.basex.core.*;
import org.basex.data.*;
import org.basex.gui.*;
import org.basex.gui.GUIConstants.Msg;
import org.basex.gui.layout.*;
import org.basex.gui.layout.BaseXFileChooser.Mode;
import org.basex.io.*;
import org.basex.io.out.*;
import org.basex.io.serial.*;
import org.basex.util.*;

/**
 * Dialog window for changing some project's preferences.
 *
 * @author BaseX Team 2005-12, BSD License
 * @author Christian Gruen
 */
public final class DialogExport extends BaseXDialog {
  /** Available encodings. */
  private static final String[] ENCODINGS;
  /** Directory path. */
  private final BaseXTextField path;
  /** Database info. */
  private final BaseXLabel info;
  /** Serialization method. */
  private final BaseXCombo method;
  /** Encoding. */
  private final BaseXCombo encoding;
  /** Buttons. */
  private final BaseXBack buttons;
  /** Parameters. */
  private final BaseXTextField params;

  // initialize encodings
  static {
    final SortedMap<String, Charset> cs = Charset.availableCharsets();
    ENCODINGS = cs.keySet().toArray(new String[cs.size()]);
  }

  /**
   * Default constructor.
   * @param main reference to the main window
   */
  public DialogExport(final GUI main) {
    super(main, EXPORT);

    // create checkboxes
    final BaseXBack p;
    if (Prop.langright) p = new BaseXBack(new RTLTableLayout(6, 1, 0, 0));
    else p = new BaseXBack(new TableLayout(4, 1, 0, 0));
    p.add(new BaseXLabel(OUTPUT_DIR + COL, true, true).border(0, 0, 6, 0));

    // output label
    BaseXBack pp;
    if (Prop.langright) pp = new BaseXBack(new RTLTableLayout(1, 2, 8, 0));
    else pp = new BaseXBack(new TableLayout(1, 2, 8, 0));

    path = new BaseXTextField(main.gprop.get(GUIProp.INPUTPATH), this);
    pp.add(path);

    final BaseXButton browse = new BaseXButton(BROWSE_D, this);
    browse.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(final ActionEvent e) { choose(); }
    });
    pp.add(browse);
    p.add(pp);

    // encoding
    final Prop prop = gui.context.prop;
    final String exporter = prop.get(Prop.EXPORTER);
    final SerializerProp sp = new SerializerProp(exporter);

    // encoding and method
    final String[] methods = new String[DataText.METHODS.length - 1];
    for(int m = 0; m < methods.length; m++) {
      methods[m] = DataText.METHODS[m].toUpperCase(Locale.ENGLISH);
    }
    method = new BaseXCombo(this, methods);
    method.setSelectedItem(sp.get(SerializerProp.S_METHOD).toUpperCase(Locale.ENGLISH));

    encoding = new BaseXCombo(this, ENCODINGS);
    String enc = gui.context.data().meta.encoding;
    boolean f = false;
    for(final String s : ENCODINGS) f |= s.equals(enc);
    if(!f) {
      enc = enc.toUpperCase(Locale.ENGLISH);
      for(final String s : ENCODINGS) f |= s.equals(enc);
    }
    encoding.setSelectedItem(f ? enc : sp.get(SerializerProp.S_ENCODING));

    params = new BaseXTextField(exporter, this);

    if (Prop.langright) {
      BaseXBack ppR = new BaseXBack(new RTLTableLayout(1, 2, 16, 6)).border(8, 0, 8, 0);
      ppR.add(new BaseXLabel(METHOD + COL, true, true));
      ppR.add(method);
      p.add(ppR);
      ppR = new BaseXBack(new RTLTableLayout(1, 2, 16, 6)).border(8, 0, 8, 0);
      ppR.add(new BaseXLabel(ENCODING + COL, true, true));
      ppR.add(encoding);
      p.add(ppR);
      ppR = new BaseXBack(new RTLTableLayout(1, 2, 16, 6)).border(8, 0, 8, 0);
      ppR.add(new BaseXLabel(PARAMETERS + COL, true, true));
      ppR.add(params);
      p.add(ppR);
    } else {
      pp = new BaseXBack(new TableLayout(3, 2, 16, 6)).border(8, 0, 8, 0);
      pp.add(new BaseXLabel(METHOD + COL, true, true));
      pp.add(method);
      p.add(pp);
      pp.add(new BaseXLabel(ENCODING + COL, true, true));
      pp.add(encoding);
      p.add(pp);
      pp.add(new BaseXLabel(PARAMETERS + COL, true, true));
      pp.add(params);
      p.add(pp);
    }
    info = new BaseXLabel(" ").border(8, 0, 0, 0);
    p.add(info);

    // indentation
    set(p, BorderLayout.CENTER);

    // buttons
    pp = new BaseXBack(new BorderLayout());
    buttons = okCancel();
    if (Prop.langright) pp.add(buttons, BorderLayout.WEST);
    else pp.add(buttons, BorderLayout.EAST);
    set(pp, BorderLayout.SOUTH);

    action(method);
    finish(null);
  }

  /**
   * Creates an encoding combo box and selects the specified encoding.
   * @param dialog dialog reference
   * @param encoding original encoding
   * @return combo box
   */
  static BaseXCombo encoding(final BaseXDialog dialog, final String encoding) {
    final BaseXCombo cb = new BaseXCombo(dialog, ENCODINGS);
    boolean f = false;
    String enc = encoding;
    for(final String s : ENCODINGS) f |= s.equals(enc);
    if(!f) {
      enc = enc.toUpperCase(Locale.ENGLISH);
      for(final String s : ENCODINGS) f |= s.equals(enc);
    }
    cb.setSelectedItem(enc);
    return cb;
  }

  /**
   * Opens a file dialog to choose an XML document or directory.
   */
  void choose() {
    final IOFile io = new BaseXFileChooser(CHOOSE_DIR, path.getText(), gui).
      select(Mode.DOPEN);
    if(io != null) path.setText(io.path());
  }

  /**
   * Returns the chosen XML file or directory path.
   * @return file or directory
   */
  public String path() {
    return path.getText().trim();
  }

  @Override
  public void action(final Object comp) {
    if(comp == method || comp == encoding) {
      final StringBuilder sb = new StringBuilder();
      // add method
      final String meth = method.getSelectedItem().toString().toLowerCase(Locale.ENGLISH);
      final boolean noxml = Token.eq(meth, DataText.M_XML, DataText.M_XHTML);
      add(sb, SerializerProp.S_METHOD, meth);
      // add encoding
      if(add(sb, SerializerProp.S_ENCODING, encoding.getSelectedItem()) && noxml) {
        // add omit-xml-declaration if encoding was set and if method is not X(HT)ML
        sb.append(',');
        sb.append(SerializerProp.S_OMIT_XML_DECLARATION[0]).append('=').append(NO);
      }
      params.setText(sb.toString());
    }

    final String pth = path();
    final IOFile io = new IOFile(pth);
    String inf = io.isDir() && io.children().length > 0 ? DIR_NOT_EMPTY : null;
    ok = !pth.isEmpty();

    if(ok) {
      gui.gprop.set(GUIProp.INPUTPATH, pth);
      if(comp == params) {
        // validate serialization parameters
        try {
          final String par = params.getText();
          Serializer.get(new ArrayOutput(), new SerializerProp(par));
          gui.set(Prop.EXPORTER, par);
        } catch(final IOException ex) {
          ok = false;
          inf = ex.getLocalizedMessage();
        }
      }
    }

    info.setText(inf, ok ? Msg.WARN : Msg.ERROR);
    enableOK(buttons, B_OK, ok);
  }

  /**
   * Adds a non-standard serialization parameter.
   * @param sb string builder
   * @param key key
   * @param val value
   * @return {@code true} if parameter was added
   */
  private static boolean add(final StringBuilder sb, final Object[] key,
      final Object val) {

    if(Serializer.PROPS.get(key).equals(val)) return false;
    if(sb.length() != 0) sb.append(',');
    sb.append(key[0]).append('=').append(val);
    return true;
  }

  @Override
  public void close() {
    if(ok) super.close();
  }
}
