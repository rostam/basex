package org.basex.gui.editor;

import java.awt.*;

import org.basex.gui.*;
import org.basex.gui.GUIConstants.Fill;
import org.basex.gui.editor.Editor.SearchDir;
import org.basex.gui.layout.*;
import org.basex.util.*;
import org.basex.util.list.*;

/**
 * Efficient Text Editor and Renderer, supporting syntax highlighting and
 * text selections.
 *
 * @author BaseX Team 2005-12, BSD License
 * @author Christian Gruen
 */
  class RTLRenderer extends Renderer {

  /**
   * @param t
   * @param b
   */
  RTLRenderer(EditorText t, BaseXBar b) {
    super(t, b);
  }
  
   protected void write(final Graphics g) {
    x = x + 100;
     if(high) {
      high = false;
    } else {
      color = isEnabled() ? syntax.getColor(text) : Color.gray;
    }

    final int ch = text.curr();
    final int cp = text.pos();
    final int cc = text.getCaret();
    if(y > 0 && y < h) {
      if(ch == TokenBuilder.MARK) {
        color = GUIConstants.GREEN;
        high = true;
      }

      // mark selected text
      if(text.selectStart()) {
        int xx = x, cw = 0;
        while(!text.inSelect() && text.more()) xx += charW(g, text.next());
        while(text.inSelect() && text.more()) cw += charW(g, text.next());
        g.setColor(GUIConstants.color(3));
        g.fillRect(xx, y - fontH * 4 / 5, cw, fontH);
        text.pos(cp);
      }

      // mark found text
      int xx = x;
      while(text.more() && text.searchStart()) {
        int cw = 0;
        while(!text.inSearch() && text.more()) xx += charW(g, text.next());
        while(text.inSearch() && text.more()) cw += charW(g, text.next());
        g.setColor(GUIConstants.color2A);
        g.fillRect(xx, y - fontH * 4 / 5, cw, fontH);
        xx += cw;
      }
      text.pos(cp);

      if(text.erroneous()) drawError(g);

      // don't write whitespaces
      if(ch > ' ') {
        g.setColor(color);
        String n = text.nextString();
        int ww = w - x;
        if(x + wordW > ww) {
          // shorten string if it cannot be completely shown (saves memory)
          int c = 0;
          for(final int nl = n.length(); c < nl && ww > 0; c++) {
            ww -= charW(g, n.charAt(c));
          }
          n = n.substring(0, c);
        }
        g.drawString(n, x, y);
      } else if(ch <= TokenBuilder.MARK) {
        g.setFont(font);
      }

      // show cursor
      if(cursor && text.edited()) {
        xx = x;
        while(text.more()) {
          if(cc == text.pos()) {
            drawCursor(g, xx);
            break;
          }
          xx += charW(g, text.next());
        }
        text.pos(cp);
      }
    }

    // handle matching parentheses
    if(ch == '(' || ch == '[' || ch == '{') {
      pars.add(x);
      pars.add(y);
      pars.add(cp);
      pars.add(ch);
    } else if((ch == ')' || ch == ']' || ch == '}') && !pars.isEmpty()) {
      final int open = ch == ')' ? '(' : ch == ']' ? '[' : '{';
      if(pars.peek() == open) {
        pars.pop();
        final int cr = pars.pop();
        final int yy = pars.pop();
        final int xx = pars.pop();
        if(cc == cp || cc == cr) {
          g.setColor(GUIConstants.color3);
          g.drawRect(xx, yy - fontH * 4 / 5, charW(g, open), fontH);
          g.drawRect(x, y - fontH * 4 / 5, charW(g, ch), fontH);
        }
      }
    }
    next();
  }

  }
