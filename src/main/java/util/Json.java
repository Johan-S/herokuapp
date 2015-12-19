/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import util.Strings.RewindableIterator;


public abstract class Json {
  public static Object parseElement(RewindableIterator it) {
    if (!it.seekNextNWS())
      return null;
    int cur = it.getPos();
    Object o = null;
    switch (it.peek()) {
      case '"': 
        o =  parseString.apply(it);
        break;
      case '[':
        o = parseList.apply(it);
        break;
      case '{':
        o = parseObject.apply(it);
        break;
      default:
        return parseNumber(it);
    }
    if (o == null)
      it.setPos(cur);
    return o;
  }
  
  
  public static boolean parsePair(RewindableIterator it, Map<String, Object> out) {
    int cur = it.getPos();
    String key = parseString.apply(it);
    if (key == null || !it.seekNextNWS() || it.next() != ':') {
      it.setPos(cur);
      return false;
    }
    Object o = parseElement(it);
    if (o == null) {
      it.setPos(cur);
      return false;
    }
    out.put(key, o);
    return true;
  }
  
  static Character parseChar(RewindableIterator it, char c) {
    if (c <= 0xf || c == 0x7f)
      return null;
    if (c == '\\')
      return parseQuotedChar(it);
    else
      return c;
  }
  static Character parseChar(RewindableIterator it) {
    if (!it.hasNext())return null;
    return parseChar(it, it.next());
  }
  static Character parseQuotedChar(RewindableIterator it, char c) {
    switch (c) {
      case '"': return '"';
      case '\\': return '\\';
      case '/': return '/';
      case 'b': return '\b';
      case 'f': return '\f';
      case 'n': return '\n';
      case 'r': return '\r';
      case 't': return '\t';
      case 'u': return parseUnicode(it);
      default : return null;
    }
  }
  static Character parseQuotedChar(RewindableIterator it) {
    if (!it.hasNext())return null;
    return parseQuotedChar(it, it.next());
  }
  static Character parseUnicode(RewindableIterator it) {
    int res = 0;
    for (int i = 0; i < 4; ++i) {
      if (!it.hasNext())
        return null;
      res = res * 16 + Parsing.toHexVal(it.next());
    }
    return (char)res;
  }
  
  private final static boolean[] validNumberChars = new boolean[128];
  static {
    for (int i = '0'; i <= '9'; ++i)
      validNumberChars[i] = true;
    validNumberChars['+'] = true;
    validNumberChars['-'] = true;
    validNumberChars['e'] = true;
    validNumberChars['E'] = true;
    validNumberChars['.'] = true;
  }
  static BigDecimal parseNumber(RewindableIterator it) {
    if(!it.seekNextNWS())
      return null;
    int beg = it.getPos();
    if (!validNumberChars[it.peek()])
      return null;
    for (char c : it)
      if (!validNumberChars[c]) {
        it.rewind();
        break;
      }
    try {
      return new BigDecimal(it.toString().substring(beg));
    } catch (NumberFormatException e) {
      it.setPos(beg);
      return null;
    }
  }
  
  
  public static <T> Function<RewindableIterator, T> encaps(char begin, char end, Function<RewindableIterator,T> f) {
    return it -> {
      int cur = it.getPos();
      if (!it.seekNextNWS() || it.next() != begin) {
        it.setPos(cur);
        return null;
      }
      T t = f.apply(it);
      if (t == null || !it.seekNextNWS() || it.next() != end) {
        it.setPos(cur);
        return null;
      }
      return t;
    };
  }
  static Function<RewindableIterator, String> parseString = encaps('"', '"', it -> {
    StringBuilder sb = new StringBuilder();
    for (char c : it) {
      if (c == '"') {
        it.rewind();
        return sb.toString();
      }
      else {
        Character ch = parseChar(it, c);
        if (ch == null)
          break;
        sb.append(ch);
      }
    }
    return null;
  });
  
  public static Function<RewindableIterator, List<Object> > parseList = encaps('[', ']', 
          it -> {
            List<Object> res = new ArrayList<>();
            for (Object o = parseElement(it); o != null; o = parseElement(it)) {
              res.add(o);
              if (!it.seekNextNWS())
                return null;
              if (it.next() != ',') {
                it.rewind();
                break;
              }
            }
            return res;
          });
  
  
  
  public static Function<RewindableIterator, Map<String, Object> > parseObject = encaps('{', '}', 
          it -> {
            Map<String, Object> res = new HashMap<>();
            for (; parsePair(it, res);) {
              if (!it.seekNextNWS())
                return null;
              if (it.next() != ',') {
                it.rewind();
                break;
              }
            }
            return res;
          });
  
}
