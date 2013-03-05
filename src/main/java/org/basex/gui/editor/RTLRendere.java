package org.basex.gui.editor;

import java.awt.*;

import org.basex.gui.*;
import org.basex.gui.layout.*;
import org.basex.util.*;

/**
 * Efficient Text Editor and Renderer, supporting syntax highlighting and
 * text selections.
 *
 * @author BaseX Team 2005-12, BSD License
 * @author Christian Gruen
 */
  class RTLRenderer extends Renderer {

    /** String containing a line of the text. */
    String line = "";

  /** Default Constructor.
   * @param t text to be drawn
   * @param b scrollbar reference
   */
  RTLRenderer(final RTLEditorText t, final BaseXBar b) {
    super(t, b);
  }

   @Override
  protected void write(final Graphics g) {
     //Font f = new Font("Times New Roman", Font.BOLD,12);
     //g.getFontMetrics()
     //g.setFont(f);
     if(high) {
      high = false;
    } else {
      color = isEnabled() ? syntax.getColor(text) : Color.gray;
    }

    final int ch = text.curr();
    //System.out.println("ali"+(char)ch);
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
      boolean test = false;
      if(ch == '\n') test = true;
      //if(ch > ' ') {
        g.setColor(color);
        String n = text.nextString();
        if(!test) line += n;
        //else line = "";
        int ww = w - x;
        if(x + wordW > ww) {
          // shorten string if it cannot be completely shown (saves memory)
          int c = 0;
          for(final int nl = n.length(); c < nl && ww > 0; c++) {
            //ww -= charW(g, n.charAt(c));
          }
          n = n.substring(0, c);
        }

        if(test) {

          if(line.contains(":")) {
          line = line.trim();
          String tilDP = line.substring(0, line.indexOf(':'));
          String afterDP = line.substring(line.indexOf(':') + 1);
          int lw = g.getFontMetrics().charsWidth(tilDP.toCharArray(), 0, tilDP.length());
          int alw = g.getFontMetrics().charsWidth(afterDP.toCharArray(), 0,
              afterDP.length());
          lw += 12;
          alw += 12;
          g.drawString(tilDP, w - lw , y);
          g.drawString(":", w - lw - 6, y);
          g.drawString(afterDP, w - lw - alw, y);
         } else {
           int lw = g.getFontMetrics().charsWidth(line.toCharArray(), 0, line.length());
           //lw += 8;
           Font ff = g.getFont();
           if(line.length() > 3)
             line = line.substring(1, line.length() - 1);
           g.setFont(new Font(g.getFont().getName(), Font.BOLD, g.getFont().getSize()));
           g.drawString(line, w - lw - 8, y);
           g.setFont(ff);
         }
          line = "";
        }
        if(ch <= TokenBuilder.MARK) {
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
