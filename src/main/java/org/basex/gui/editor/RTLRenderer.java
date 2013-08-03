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
    /** RTL Functions. */
    RTLFunctions rtl = new RTLFunctions();
  /** Default Constructor.
   * @param t text to be drawn
   * @param b scrollbar reference
   */
  RTLRenderer(final EditorText t, final BaseXBar b) {
    super(t, b);
  }

  @Override
  public void paintComponent(final Graphics g) {
    super.paintComponent(g);

    pars.reset();
    init(g, bar.pos());
    line = "";
    while(more(g)) write(g);
    wordW = 0;
    final int s = text.size();
    if(cursor && s == text.getCaret()) drawCursor(g, x);
    if(s == text.error()) drawError(g);
  }

   @Override
  protected void write(final Graphics g) {
     //Font f = new Font("Times New Roman", Font.BOLD,12);
     //g.getFontMetrics()
     //g.setFont(f);
     System.out.println("Salam");
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
//    if(text.selectStart()) {
//      int xx = x, cw = 0;
//      while(!text.inSelect() && text.more()) xx -= charW(g, text.next());
//      while(text.inSelect() && text.more()) cw -= charW(g, text.next());
//      g.setColor(GUIConstants.color(3));
//      g.fillRect(w - xx, y - fontH * 4 / 5, cw, fontH);
//      text.pos(cp);
//    }

    // mark found text
    int xx = x;
    while(text.more() && text.searchStart()) {
      int cw = 0;
      while(!text.inSearch() && text.more()) xx -= charW(g, text.next());
      while(text.inSearch() && text.more()) cw += charW(g, text.next());
      g.setColor(GUIConstants.color2A);
      g.fillRect(xx, y - fontH * 4 / 5, cw, fontH);
      xx += cw;
    }
    text.pos(cp);

    if(text.erroneous()) drawError(g);

    // don't write whitespaces
    boolean test = false;
    //if (text.moreTokens()) {
      if(ch == '\n') test = true;
      g.setColor(color);
      String n = text.nextString();
//      if(cp == text.size() - 1) test = true;
//      if(cp == text.size() - 2) test = true;
      byte[] tmp = text.text();
      System.out.println("text: " + tmp.toString());
      //if (cp == 0) test = true;
      if(!test) line += n;
      if(cp == text.size()) test = true;
      if(cp == text.size() - 1) test = true;
      if(cp == text.size() - 2) test = true;
      System.out.println(cp + " = " + text.size());

      int ww = w - x;
      if(x + wordW > ww) {
        // shorten string if it cannot be completely shown (saves memory)
        int c = 0;
        for(final int nl = n.length(); c < nl && ww > 0; c++) {
          ww -= charW(g, n.charAt(c));
        }
        n = n.substring(0, c);
      }

      if (test) {

        if(rtl.rtlChar(line)) {
        System.out.println("chetori: " + line);
        line = line.trim();
        int lw = g.getFontMetrics().charsWidth(line.toCharArray(), 0, line.length());

        if (line.contains(":") && !line.contains("://")) {
          String tilDP = line.substring(0, line.indexOf(':'));
          String afterDP = line.substring(line.indexOf(':') + 1);
          if (tilDP.charAt(0) == '-'
              && Character.toLowerCase(tilDP.charAt(2)) >= 'a'
              && Character.toLowerCase(tilDP.charAt(2)) <= 'z')
              tilDP = tilDP.substring(1) + " -";
          int frstL = g.getFontMetrics().charsWidth(tilDP.toCharArray(), 0, tilDP.length());
          int scndL = g.getFontMetrics().charsWidth(afterDP.toCharArray(), 0,
              afterDP.length());
          frstL += 12;
          scndL += 12;
          g.drawString(tilDP, w - frstL , y);
          g.drawString(":", w - frstL - 6, y);
          g.drawString(afterDP, w - frstL - scndL, y);
        } else g.drawString(line, w - lw , y);
       } else {
           Font ff = g.getFont();
           if(line.length() >= 1) {
           if(line.charAt(0) == 2) {
              g.setFont(new Font(g.getFont().getName(),
                  Font.BOLD, g.getFont().getSize()));
             line = line.substring(1);
         }
         if(line.length() >= 1)
           if(line.charAt(line.length() - 1) == 3)
             line = line.substring(0, line.length() - 1);
         }
         //g.drawString(line, w-lw-8 , y);
           g.drawString(line, 8, y);
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
          final int xX = pars.pop();
          if(cc == cp || cc == cr) {
            g.setColor(GUIConstants.color3);
            g.drawRect(xX, yy - fontH * 4 / 5, charW(g, open), fontH);
            g.drawRect(x, y - fontH * 4 / 5, charW(g, ch), fontH);
          }
        }
      }
      next();
    }
   }
 // }

   /**
    * Paints the text cursor.
    * @param g graphics reference
    * @param xx x position
    */
   @Override
  protected void drawCursor(final Graphics g, final int xx) {
     g.setColor(GUIConstants.DGRAY);
     //g.fillRect(w - xx, y - fontH * 4 / 5, 2, fontH);
   }

   /**
    * Selects the text at the specified position.
    * @param pos current text position
    * @param p mouse position
    * @param finish states if selection is in progress
    */
   //@Override
//  void select(final int pos, final Point p, final boolean finish) {
//     if(!finish) text.noSelect();
//     p.y -= 3;
//
//     final Graphics g = getGraphics();
//     init(g, pos);
//     if(p.y > y - fontH) {
//       int s = text.pos();
//       while(true) {
//         // end of line
//         if(p.x > x && p.y < y - fontH) {
//           text.pos(s);
//           break;
//         }
//         // end of text - skip last characters
//         if(!more(g)) {
//           while(text.more()) text.next();
//           break;
//         }
//         // beginning of line
//         if(p.x >= x && p.y < y) break;
//         // middle of line
//         if(p.x > x && p.x >= x + wordW && p.y > y - fontH && p.y <= y) {
//           while(text.more()) {
//             final int ww = charW(g, text.curr());
//             if(p.x < x + ww) break;
//             x += ww;
//             text.next();
//           }
//           break;
//         }
//         s = text.pos();
//         next();
//       }
//
//       if(!finish) text.startSelect();
//       else text.extendSelect();
//       text.setCaret();
//     }
//     repaint();
//   }

  }
