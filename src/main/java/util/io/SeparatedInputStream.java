package util.io;

import java.io.IOException;
import java.io.InputStream;
import util.KMP;


public class SeparatedInputStream extends InputStream {

  private final InputStream stream;
  private int[] buffer;
  private int curPos = 0;
  private int curMatch = 0;
  private final KMP separator;

  private boolean isDone() {
    return separator.isDone(curMatch) || buffer[curPos] == -1;
  }

  public SeparatedInputStream(InputStream in, KMP separator) throws IOException {
    this.separator = separator;
    this.stream = in;
    buffer = new int[separator.length()];
    for (int i = 0; i < buffer.length; ++i) {
      buffer[i] = in.read();
      if (buffer[i] == -1) {
        break;
      }
      curMatch = separator.step((byte) buffer[i], curMatch);
    }
  }

  @Override
  public int read() throws IOException {
    if (isDone()) {
      return -1;
    }
    int res = buffer[curPos];
    buffer[curPos] = stream.read();
    curMatch = separator.step((byte)buffer[curPos++], curMatch);
    if (curPos == buffer.length) {
      curPos = 0;
    }
    return res;
  }
}
