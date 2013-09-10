package org.basex.gui.editor;

public class RTLFunctions {
  
  /**
   * Checks if a string contains any rtl character(s).
   * @return result of check
   */
  public boolean rtlChar (final String s) {
    char[] chars = s.toCharArray();
    for(char c: chars){
        if(c >= 0x600 && c <= 0x6ff){
          //Text contains RTL character
          return true;
        }
    }
    return false;
  }

}
