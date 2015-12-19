/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.Locale;
import java.util.TimeZone;
import java.util.function.Function;
import java.util.logging.Level;
import java.util.logging.Logger;


public abstract class Strings {

  static final byte[] hex = "0123456789abcdef".getBytes();

  public static String toHex(byte b[]) {
    byte[] res = new byte[b.length * 2];
    for (int i = 0; i < b.length; ++i) {
      res[2 * i] = hex[(b[i] >> 4) & 0xf];
      res[2 * i + 1] = hex[b[i] & 0xf];
    }
    return new String(res);
  }

  public static String toQuotedSQLString(String s) {
    StringBuilder res = new StringBuilder("'");
    for (int i = 0; i < s.length(); ++i) {
      char c = s.charAt(i);
      res.append(c);
      if (c == '\'') {
        res.append(c);
      }
    }
    res.append('\'');
    return res.toString();
  }

  public static String formatSQL(String pattern, Object... args) {
    for (int i = 0; i < args.length; ++i) {
      if (args[i] instanceof String) {
        args[i] = Strings.toQuotedSQLString((String) args[i]);
      }
    }
    return String.format(pattern, args);
  }
  public static String fromDate(Date d) {
    SimpleDateFormat dateFormat = new SimpleDateFormat(
          "EEE, dd MMM yyyy HH:mm:ss z", Locale.US);
    dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
    return dateFormat.format(d);
  }
  public static Date toDate(String s) {
    SimpleDateFormat dateFormat = new SimpleDateFormat(
          "EEE, dd MMM yyyy HH:mm:ss z", Locale.US);
    dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
    if (s == null || s.equals(""))
      return new Date(-1000);
    try {
      return dateFormat.parse(s);
    } catch (ParseException ex) {
      Logger.getLogger(Strings.class.getName()).log(Level.SEVERE, "String: " + s, ex);
      return new Date(-1000);
    }
  }
  
  public static class RewindableIterator implements Iterator<Character>, Iterable<Character> {
    private final String val;
    private int cur = 0;
    RewindableIterator(String s) {
      val = s;
    }
    @Override
    public boolean hasNext() {
      return cur < val.length();
    }
    
    public boolean seekNextNWS() {
      while(cur < val.length() && val.charAt(cur) <= ' ')
        cur++;
      return cur < val.length();
    }

    @Override
    public Character next() {
      return val.charAt(cur++);
    }
    
    public Character peek() {
      return val.charAt(cur);
    }
    
    public void rewind() {
      cur--;
    }
    
    public int getPos() {
      return cur;
    }
    public void setPos(int pos) {
      cur = pos;
    }

    @Override
    public Iterator<Character> iterator() {
      return this;
    }
    
    @Override
    public String toString() {
      return val.substring(0, cur);
    }
  }
  
  public static RewindableIterator iter(String s) {
    return new RewindableIterator(s);
  }
  
}
