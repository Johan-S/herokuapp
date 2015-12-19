package util.io;

import java.io.IOException;
import java.io.InputStream;


public class Streams {
  
  public static String readWindowsLine(InputStream in) throws IOException {
    String res = takeUntil(in, '\r');
    in.read();
    return res;
  }
  
  public static String takeUntil(InputStream in, char end) throws IOException {
    StringBuilder sb = new StringBuilder();
    int c;
    while ((c = in.read()) != end && c != -1)
      sb.append((char)c);
    return sb.toString();
  }
  
}
