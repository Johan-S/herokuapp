/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import java.util.Arrays;
import java.util.function.*;


public class Functional {
  public static <T,R> R[] map(T[] in, Function<T,R> f) throws Exception {
    R[] res = (R[])(new Object[in.length]);
    for (int i = 0; i < in.length; ++i)
      res[i] = f.apply(in[i]);
    return res;
  }
  public static <T> T[] filter(T[] in, Predicate<T> f) throws Exception {
    T[] part = (T[])(new Object[in.length]);
    int tot = 0;
    for (T t : in)
      if (f.test(t))
        part[tot++] = t;
    return Arrays.copyOf(part, tot);
  }
}
