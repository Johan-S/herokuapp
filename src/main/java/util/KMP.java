package util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

/**
 * Creates an instance of the Knuth-Morris-Pratt algorithm
 * @author Johan Strååt
 */
public class KMP {
  private final byte[] pattern;
  private final int[] matchingPrefixSize;
  
  public KMP(byte[] pattern) {
    this.pattern = Arrays.copyOf(pattern, pattern.length);
    matchingPrefixSize = new int[pattern.length + 1];
    matchingPrefixSize[0] = -1;
    for (int i = 0; i < pattern.length; ++i)
      matchingPrefixSize[i + 1] = step(pattern[i], matchingPrefixSize[i]);
  }
  
  public int length() {
    return pattern.length;
  }
  
  /**
   * @param value the value you want to match against
   * @param position the current matching size
   * @return the current match size
   */
  public final int step(byte value, int position) {
    while (position >= 0 && value != pattern[position])
      position = matchingPrefixSize[position];
    return position + 1;
  }
  
  public final boolean isDone(int position) {
    return pattern.length == position;
  }
}
