/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import util.Strings.RewindableIterator;


public class Parsing {
  public static long parseLongHex(String hex) {
    return parseLongHex(hex, 0, hex.length());
  }
  public static long parseLongHex(String hex, int start, int end) {
    long res = 0;
    for (;start < end; ++start)
      res = (res << 4) + rhex[hex.charAt(start)];
    return res;
  }
  public static long parseLongHex(RewindableIterator hex, int len) {
    long res = 0;
    for (int i = 0; i < len; ++i)
      res = (res << 4) + rhex[hex.next()];
    return res;
  }
  public static byte[] toHexArray(long value) {
    long r = 0xf;
    int n = 15;
    while (n != 0 && (r & value >>> (n * 4)) == 0)
      n--;
    byte[] res = new byte[n + 1];
    for (int i = 0; n - i >= 0; ++i)
      res[i] = hexValues[(int)(value >>> ((n - i) * 4) & 15)];
    return res;
  }
  public static String toHexString(long value) {
    return new String(toHexArray(value));
  }
  public static int toHexVal(char c) {
    return rhex[c];
  }
  
  private static final byte[] hexValues = "0123456789ABCDEF".getBytes();
  private static final byte[] hexLowerValues = "0123456789abcdef".getBytes();
  private static final byte[] rhex = new byte[128];
  
  static {
    for (int i = 0; i < 16; ++i) {
      rhex[hexValues[i]] = (byte)i;
      rhex[hexLowerValues[i]] = (byte)i;
    }
  }
  
}
