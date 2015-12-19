package webapp;

import httpserver.Request;
import httpserver.Response;
import static httpserver.Responses.json;
import static httpserver.Responses.respond;
import static httpserver.Responses.safeFile;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;
import util.Strings;


public class Sources {
  static final String JAVA_SOURCE = "src/";
  static List<String> getFileList(String path, File folder, List<String> part) {
    File[] listOfFiles = folder.listFiles();
    for (File f : listOfFiles) {
      if (f.isFile()) {
        part.add(path + f.getName());
      } else if(f.isDirectory()) {
        getFileList(path + f.getName() + '/', f, part);
      }
    }
    return part;
  }
  static List<String> getFileList() {
    List<String> res = new ArrayList<>();
    File folder = new File(Router.WEB_PATH);
    getFileList("", folder, res);
    folder = new File(JAVA_SOURCE);
    getFileList("java/", folder, res);
    return res;
  }
  
  
  
  public static Response javaFileResponse(Request req) {
    return respond(safeFile(JAVA_SOURCE, req.path.substring(5)), Strings.toDate(req.headers.get("If-Modified-Since")));
  }
  public static Response sourceFileList(Request req) {
      StringJoiner sj = new StringJoiner("\",\"", "\"", "\"");
      getFileList().stream().forEach(s -> {
        sj.add(s);
      });
      return json("[" + sj.toString() + "]");
  }
  
}
