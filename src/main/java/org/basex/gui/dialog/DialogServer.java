package org.basex.gui.dialog;

import static org.basex.core.Text.*;
import static org.basex.gui.GUIConstants.*;

import java.awt.*;
import java.awt.event.*;
import java.io.*;

import javax.swing.event.*;

import org.basex.*;
import org.basex.core.*;
import org.basex.core.cmd.*;
import org.basex.gui.*;
import org.basex.gui.GUIConstants.Msg;
import org.basex.gui.editor.*;
import org.basex.gui.layout.*;
import org.basex.io.*;
import org.basex.server.*;
import org.basex.util.*;
import org.basex.util.list.*;

/**
 * Dialog window for displaying information about the server.
 *
 * @author BaseX Team 2005-12, BSD License
 * @author Andreas Weiler
 */
public final class DialogServer extends BaseXDialog {
  /** Password textfield. */
  private final BaseXPassword admpass;

  /** Tabulators. */
  final BaseXTabs tabs;
  /** User panel. */
  final DialogUser user = new DialogUser(true, this);
  /** Databases panel. */
  final DialogUser dbsP = new DialogUser(false, this);
  /** Databases panel. */
  final BaseXBack databases = dbsP.getTablePanel();
  /** Sessions/Databases panel. */
  final BaseXBack sess = new BaseXBack();
  /** Log panel. */
  final BaseXBack logs = new BaseXBack();
  /** Username textfield. */
  final BaseXTextField admuser;
  /** Disconnect button. */
  final BaseXButton disconnect;
  /** Refresh button. */
  final BaseXButton refreshSess;
  /** Connect button. */
  final BaseXButton connect;
  /** Start button. */
  final BaseXButton start;
  /** Indicates which tab is activated. */
  int tab;

  /** Context. */
  private final Context ctx = gui.context;
  /** Updates log file. */
  private final BaseXButton refreshLog;
  /** Stop button. */
  private final BaseXButton stop;
  /** Deletes log file. */
  private final BaseXButton delete;
  /** Deletes all log files. */
  private final BaseXButton deleteAll;
  /** Server host. */
  private final BaseXTextField host;
  /** Local server port. */
  private final BaseXTextField ports;
  /** Server port. */
  private final BaseXTextField portc;
  /** Current sessions. */
  private final Editor sese;
  /** Log text. */
  private final Editor logt;
  /** Info label. */
  private final BaseXLabel infoC;
  /** Info label. */
  private final BaseXLabel infoL;
  /** Combobox for log files. */
  private final BaseXCombo logc;
  /** String for log dir. */
  private final IOFile logd = ctx.mprop.dbpath(".logs");
  /** ClientSession. */
  private ClientSession cs;
  /** Boolean for check is server is running. */
  private boolean running;
  /** Boolean for check if client is connected. */
  private boolean connected;

  /**
   * Default constructor.
   * @param main reference to the main window
   */
  public DialogServer(final GUI main) {
    super(main, S_SERVER_ADMIN);
    databases.border(8);

    // connection tab
    final BaseXBack conn = new BaseXBack(new BorderLayout(0, 32)).border(8);
    start = new BaseXButton(START, this);
    stop = new BaseXButton(STOP, this);
    connect = new BaseXButton(CONNECT, this);
    disconnect = new BaseXButton(DISCONNECT, this);

    final GUIProp gprop = gui.gprop;
    host = new BaseXTextField(gprop.get(GUIProp.S_HOST), this);
    ports = new BaseXTextField(Integer.toString(gprop.num(GUIProp.S_SERVERPORT)), this);
    portc = new BaseXTextField(Integer.toString(gprop.num(GUIProp.S_PORT)), this);
    admuser = new BaseXTextField(gprop.get(GUIProp.S_USER), this);
    admpass = new BaseXPassword(this);
    infoC = new BaseXLabel(" ").border(12, 0, 0, 0);

    // local server panel
    BaseXBack p;
    if(Prop.langright) p = new BaseXBack(new RTLTableLayout(6, 1, 0, 0));
    else p = new BaseXBack(new TableLayout(6, 1, 0, 0));
    p.add(new BaseXLabel(S_LOCALSERVER + COLS, true, true));

    BaseXBack pp;
    if (Prop.langright) pp = new BaseXBack(new RTLTableLayout(2, 2, 8, 4)).border(0, 0, 0, 0);
    else pp = new BaseXBack(new TableLayout(2, 2, 8, 4)).border(0, 0, 0, 0);
    pp.add(new BaseXLabel(S_PORT + COLS));
    pp.add(ports);
    if (Prop.langright) pp.add(new BaseXLabel("        "));
    else pp.add(new BaseXLabel());
    BaseXBack ppp;
    if (Prop.langright) ppp = new BaseXBack(new RTLTableLayout(1, 2, 5, 0));
    else ppp = new BaseXBack(new TableLayout(1, 2, 5, 0));
    ppp.add(start);
    ppp.add(stop);
    pp.add(ppp);
    p.add(pp);

    p.add(new BaseXLabel());
    p.add(new BaseXLabel(S_ADLOGIN + COLS, true, true).border(12, 0, 6, 0));

    // login panel
    if (Prop.langright) pp = new BaseXBack(new RTLTableLayout(5, 2, 8, 4));
    else pp = new BaseXBack(new TableLayout(5, 2, 8, 4));
    pp.add(new BaseXLabel(USERNAME + COLS));
    pp.add(admuser);
    pp.add(new BaseXLabel(PASSWORD + COLS));
    pp.add(admpass);
    pp.add(new BaseXLabel(S_HOST + COLS));
    pp.add(host);
    pp.add(new BaseXLabel(S_PORT + COLS));
    pp.add(portc);
    if (Prop.langright) pp.add(new BaseXLabel("        "));
    else pp.add(new BaseXLabel());
    if (Prop.langright) ppp = new BaseXBack(new RTLTableLayout(1, 2, 5, 0));
    else ppp = new BaseXBack(new TableLayout(1, 2, 5, 0));
    ppp.add(connect);
    ppp.add(disconnect);
    pp.add(ppp);
    p.add(pp);
    p.add(infoC);

    conn.add(p, BorderLayout.CENTER);

    if (Prop.langright) p = new BaseXBack(new RTLTableLayout(2, 1));
    else p = new BaseXBack(new TableLayout(2, 1));
    BaseXLabel l = new BaseXLabel(S_INFO1);
    l.setForeground(GUIConstants.DGRAY);
    p.add(l);
    l = new BaseXLabel(S_INFO2);
    l.setForeground(GUIConstants.DGRAY);
    p.add(l);
    conn.add(p, BorderLayout.SOUTH);

    // session tab
    sess.border(8).layout(new BorderLayout());
    sese = new Editor(false, this);
    sese.setFont(start.getFont());
    refreshSess = new BaseXButton(REFRESH, this);

    pp = new BaseXBack(new BorderLayout());
    pp.add(new BaseXLabel(S_SESSIONS + COLS, false, true), BorderLayout.NORTH);
    pp.add(new SearchEditor(main, sese), BorderLayout.CENTER);
    p.add(pp);
    sess.add(pp, BorderLayout.CENTER);

    p = new BaseXBack(new BorderLayout(0, 0));
    p.add(refreshSess, BorderLayout.EAST);
    sess.add(p, BorderLayout.SOUTH);

    // logging tab
    logs.border(8).layout(new BorderLayout());
    delete = new BaseXButton(DELETE, this);
    deleteAll = new BaseXButton(DELETE_ALL, this);

    logc = new BaseXCombo(this);
    logt = new Editor(false, this);
    logt.setFont(start.getFont());
    BaseXLayout.setHeight(logt, 100);

    logt.border(5);
    infoL = new BaseXLabel(" ").border(12, 0, 0, 0);
    refreshLog = new BaseXButton(REFRESH, this);

    p = new BaseXBack(new BorderLayout());
    pp = new BaseXBack();
    pp.add(logc);
    pp.add(delete);
    pp.add(deleteAll);
    if (Prop.langright) p.add(pp, BorderLayout.EAST);
    else p.add(pp, BorderLayout.WEST);
    logs.add(p, BorderLayout.NORTH);
    logs.add(new SearchEditor(main, logt), BorderLayout.CENTER);

    p = new BaseXBack(new BorderLayout(8, 0));
    p.add(infoL, BorderLayout.WEST);
    p.add(refreshLog, BorderLayout.EAST);

    logs.add(p, BorderLayout.SOUTH);

    tabs = new BaseXTabs(this);
    tabs.add(S_CONNECT, conn);
    tabs.add(USERS, user);
    tabs.add(DATABASES, databases);
    tabs.add(S_SESSIONS, sess);
    tabs.add(S_LOCALLOGS, logs);
    set(tabs, BorderLayout.CENTER);

    // test if server is running
    running = ping(true);

    tabs.addChangeListener(new ChangeListener() {
      @Override
      public void stateChanged(final ChangeEvent evt) {
        final BaseXTabs pane = (BaseXTabs) evt.getSource();
        tab = pane.getSelectedIndex();
        final Object o = pane.getSelectedComponent();
        if(o == logs) refreshLog();
        if(o == user) action(user);
        if(o == databases) action(dbsP);
        if(o == sess) action(refreshSess);
      }
    });

    // start server: when enter key is pressed, the server will be started
    final KeyAdapter startListener = new KeyAdapter() {
      @Override
      public void keyPressed(final KeyEvent key) {
        if(key.getKeyCode() == KeyEvent.VK_ENTER) action(start);
      }
    };
    ports.addKeyListener(startListener);

    // admin login: when enter key is pressed the connect button will be clicked
    final KeyAdapter connListener = new KeyAdapter() {
      @Override
      public void keyPressed(final KeyEvent key) {
        if(key.getKeyCode() == KeyEvent.VK_ENTER) action(connect);
      }
    };
    admuser.addKeyListener(connListener);
    admpass.addKeyListener(connListener);
    host.addKeyListener(connListener);
    portc.addKeyListener(connListener);

    refreshLog();
    action(null);
    setResizable(true);
    finish(null);
  }

  /**
   * Checks if a server is running, ping like.
   * @param local local/remote flag
   * @return boolean success
   */
  private boolean ping(final boolean local) {
    final GUIProp gprop = gui.gprop;
    return BaseXServer.ping(local ? LOCALHOST : gprop.get(GUIProp.S_HOST),
      gprop.num(local ? GUIProp.S_SERVERPORT : GUIProp.S_PORT));
  }

  @Override
  public void action(final Object cmp) {
    Msg icon = Msg.SUCCESS;
    String msg = null;
    String msg2 = null;

    final boolean wait = cmp == start || cmp == stop || cmp == connect;
    if(wait) setCursor(CURSORWAIT);

    try {
      if(cmp == start) {
        try {
          final int p = Integer.parseInt(ports.getText());
          gui.gprop.set(GUIProp.S_SERVERPORT, p);
          if(host.getText().equals(LOCALHOST)) {
            gui.gprop.set(GUIProp.S_PORT, p);
            gui.gprop.set(GUIProp.S_EVENTPORT, p + 1);
            portc.setText(String.valueOf(p));
          }
          BaseXServer.start(p, "-p", Integer.toString(p), "-e", Integer.toString(p + 1));
          msg = SRV_STARTED;
          running = true;
        } catch(final BaseXException ex) {
          msg = ex.getMessage();
          icon = Msg.ERROR;
        }
      } else if(cmp == stop) {
        if(running) BaseXServer.stop(gui.gprop.num(GUIProp.S_SERVERPORT),
            gui.gprop.num(GUIProp.S_EVENTPORT));
        running = ping(true);
        connected = connected && ping(false);
        if(!connected) msg = SRV_STOPPED;
        if(!connected) setTitle(S_SERVER_ADMIN);
      } else if(cmp == connect) {
        final String pw = new String(admpass.getPassword());
        final String us = admuser.getText();
        final String hs = host.getText();
        final int pc = Integer.parseInt(portc.getText());
        gui.gprop.set(GUIProp.S_HOST, hs);
        gui.gprop.set(GUIProp.S_PORT, pc);
        gui.gprop.set(GUIProp.S_USER, us);
        gui.gprop.set(GUIProp.S_PASSWORD, pw);
        cs = new ClientSession(ctx, us, pw);
        user.setSess(cs);
        dbsP.setSess(cs);
        connected = true;
        setTitle(S_SERVER_ADMIN + LI + us + '@' + hs + COL + pc);
        msg = Util.info(S_CONNECTED, hs, pc);
        refreshSess();
        disconnect.requestFocusInWindow();
      } else if(cmp == disconnect) {
        cs.execute(new Exit());
        connected = false;
        setTitle(S_SERVER_ADMIN);
        msg = S_DISCONNECTED;
      } else if(cmp == refreshSess) {
        refreshSess();
      } else if(cmp == refreshLog || cmp == logc) {
        byte[] cont = Token.EMPTY;
        if(logc.getSelectedIndex() != -1) {
          final IOFile f = new IOFile(logd, logc.getSelectedItem().toString());
          cont = f.read();
        }
        logt.setText(cont);
        logt.scrollToEnd();
      } else if(cmp == delete) {
        final IOFile f = new IOFile(logd, logc.getSelectedItem().toString());
        if(f.delete()) {
          logc.setSelectedIndex(-1);
          refreshLog();
        } else {
          msg2 = Util.info(FILE_NOT_DELETED_X, f.name());
          icon = Msg.ERROR;
        }
      } else if(cmp == deleteAll) {
        IOFile file = null;
        for(int i = 0; i < logc.getItemCount(); ++i) {
          final IOFile f = new IOFile(logd, logc.getItemAt(i).toString());
          if(!f.delete()) file = f;
        }
        if(file != null) {
          msg2 = Util.info(FILE_NOT_DELETED_X, file.name());
          icon = Msg.ERROR;
        }
        logc.setSelectedIndex(-1);
        refreshLog();
      } else if(connected) {
        if(tab == 1) user.action(cmp);
        if(tab == 2) dbsP.action(cmp);
      }
    } catch(final Exception ex) {
      icon = Msg.ERROR;
      msg = Util.message(ex);
      if(msg.equals(Util.info(PERM_REQUIRED_X, Perm.ADMIN))) {
        try {
          cs.execute(new Exit());
        } catch(final IOException exx) {
          Util.stack(exx);
        }
      }
    }
    if(wait) setCursor(CURSORARROW);

    final boolean valp = portc.getText().matches("[\\d]+") &&
      Integer.parseInt(portc.getText()) <= 65535;
    final boolean valpl = ports.getText().matches("[\\d]+") &&
      Integer.parseInt(ports.getText()) <= 65535;
    final boolean vallu = admuser.getText().matches("[\\w]*");
    final boolean vallp = new String(admpass.getPassword()).matches("[^ ;'\"]*");
    final boolean valh = host.getText().matches("([\\w]+://)?[\\w.-]+");

    if(msg == null && msg2 == null &&
        !(valpl && valh && valp && vallu && vallp)) {
      msg = Util.info(INVALID_X, !valpl ? S_LOCALPORT : !valh ? S_HOST :
        !valp ? S_PORT : !vallu ? USERNAME : PASSWORD);
      icon = Msg.WARN;
    }
    infoC.setText(msg, icon);
    infoL.setText(msg2, icon);

    ports.setEnabled(!running);
    start.setEnabled(!running && valpl);
    stop.setEnabled(running);
    admuser.setEnabled(!connected);
    admpass.setEnabled(!connected);
    host.setEnabled(!connected);
    portc.setEnabled(!connected);
    connect.setEnabled(!connected && vallu && vallp && valh && valp &&
        !admuser.getText().isEmpty() && admpass.getPassword().length != 0);
    disconnect.setEnabled(connected);
    tabs.setEnabledAt(1, connected);
    tabs.setEnabledAt(2, connected);
    tabs.setEnabledAt(3, connected);
    tabs.setEnabledAt(4, running || logc.getItemCount() > 0);
    refreshLog.setEnabled(logc.getSelectedIndex() != -1);
    delete.setEnabled(logc.getSelectedIndex() != -1);
    deleteAll.setEnabled(logc.getItemCount() > 0);
    if(admuser.hasFocus()) connect.setEnabled(false);
  }

  /**
   * Fills sessions/databases panel.
   * @throws IOException I/O exception
   */
  private void refreshSess() throws IOException {
    sese.setText(Token.token(cs.execute(new ShowSessions())));
  }

  /**
   * Refreshes the log panel.
   */
  void refreshLog() {
    logc.removeAllItems();
    final StringList sl = new StringList();
    for(final IOFile s : logd.children()) {
      final String name = s.name();
      if(name.endsWith(".log")) sl.add(name);
    }
    for(final String s : sl.sort(false, false)) logc.addItem(s);
    action(refreshLog);
  }

  @Override
  public void cancel() {
    try {
      if(connected) cs.execute(new Exit());
    } catch(final IOException ex) {
      Util.debug(ex);
    }
    super.cancel();
  }

  @Override
  public void close() {
    if(ok) super.close();
  }
}
