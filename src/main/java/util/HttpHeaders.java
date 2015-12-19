package util;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import util.io.Streams;


public class HttpHeaders {
  public static Map<String, String> parseHeaders(InputStream in) throws IOException {
    Map<String, String> res = new HashMap<>();
    String line;
    while (!(line = Streams.readWindowsLine(in)).equals("")) {
      int pos = line.indexOf(':');
      res.put(line.substring(0, pos), line.substring(pos+2));
    }
    return res;
  }
  public static Map<String, String> parseHeaders(List<String> in) throws IOException {
    Map<String, String> res = new HashMap<>();
    for(String line : in) {
      int pos = line.indexOf(':');
      res.put(line.substring(0, pos), line.substring(pos+2));
    }
    return res;
  }
}
