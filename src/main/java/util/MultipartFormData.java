/*
 * The MIT License
 *
 * Copyright 2015 Johan Strååt.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package util;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import util.io.SeparatedInputStream;
import util.io.Streams;


public class MultipartFormData {
  
  public List<FormData> data = new ArrayList<>();
  
  private MultipartFormData () {
    
  }
  
  private void addData(InputStream in) throws IOException {
    FormData d = new FormData();
    Map<String, String> headers = HttpHeaders.parseHeaders(in);
    String hs = headers.get("Content-Disposition");
    String[] disp = hs.split("; ");
    for (int i = 1; i < disp.length; ++i) {
      String[] kv = disp[i].split("=");
      switch(kv[0]) {
        case "name":
          d.name = kv[1].substring(1, kv[1].length()-1);
          break;
        case "filename":
          d.fileName = kv[1].substring(1, kv[1].length()-1);
          break;
      }
    }
    d.contentType = headers.get("Content-Type");
    {
      byte[] b = new byte[10];
      int sz = 0;
      int c;
      while ((c = in.read()) != -1) {
        b[sz++] = (byte)c;
        if (sz == b.length)
          b = Arrays.copyOf(b, b.length * 2);
      }
      d.data = Arrays.copyOf(b, sz);
    }
    data.add(d);
  }
  
  public static MultipartFormData parse(InputStream in, byte[] separator) throws IOException {
    MultipartFormData res = new MultipartFormData();
    KMP sep = new KMP(separator);
    Streams.readWindowsLine(in);
    do {
      res.addData(new SeparatedInputStream(in, sep));
    } while(in.read() + in.read() == '\n' + '\r');
    return res;
  }
  
  public static class FormData {
    public String name, fileName;
    public String contentType;
    public byte[] data;
    
  }
  
}
