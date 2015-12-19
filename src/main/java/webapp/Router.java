
package webapp;

import httpserver.RequestHandler;
import static httpserver.RequestHandler.*;
import static httpserver.Responses.*;
import static httpserver.Http.Method.*;
import static httpserver.sql.SQLResponses.directUniqueSQL;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import util.Json;
import util.Strings;


public class Router {
  static final String WEB_PATH = "web/";
  
  static final String UPLOADED_PUBLIC_PATH = "uploaded_public_files/";
    
  static byte[] readAll(String path) {
    try(InputStream in = new FileInputStream(path)) {
      byte[] b = new byte[1<<18];
      int r = in.read(b);
      return Arrays.copyOf(b, r);
    } catch (Exception ex) {
      Logger.getLogger(Router.class.getName()).log(Level.SEVERE, null, ex);
    }
    return null;
  }
  
  static RequestHandler get() {
    Aggregate.Builder ab = new Aggregate.Builder();
    ab.add(GET, "/me", req -> 
            directUniqueSQL(Strings.formatSQL("SELECT name, account_id as id FROM session JOIN account ON account.id = account_id WHERE session.id = %s;",req.cookies.get("session")))
    );
    ab.add(GET, "/files", req -> Sources.sourceFileList(req));
    ab.add(GET, "/uploaded(/.*)", req -> respond(safeFile(UPLOADED_PUBLIC_PATH, req.params.get(0))));
    ab.add(GET, "/java.*", req -> Sources.javaFileResponse(req));
    ab.add(GET, "/imagelist", req -> Images.publicImageList(req));
    ab.add(GET, "/.+", req -> respond(safeFile(WEB_PATH, req.path), Strings.toDate(req.headers.get("If-Modified-Since"))));
    
    final byte[] index = readAll("web/index.html");
    
    ab.add(GET, "/", req -> new RawResponse(index, "text/html"));
    
    
    //ab.add(GET, "/", req -> respond(new File("web/index.html"), Strings.toDate(req.headers.get("If-Modified-Since"))));
    ab.add(POST, "/login", req -> {
      Map<String, Object> m;
      m = Json.parseObject.apply(Strings.iter(new String(req.data)));
      long id = Account.login((String)m.get("name"),(String)m.get("password"));
      return json(String.format("{\"name\":\"%s\",\"id\":%d}", m.get("name"), id))
              .setCookie("session", new Session(id).sessionId, 300);
    });
    ab.add(POST, "/register", req -> {
      Map<String, Object> m;
      m = Json.parseObject.apply(Strings.iter(new String(req.data)));
      long id = Account.create((String)m.get("name"),(String)m.get("password"));
      return json(String.format("{\"name\":\"%s\",\"id\":%d}", m.get("name"), id))
              .setCookie("session", new Session(id).sessionId, 300);
    });
    ab.add(POST, "/upload", req -> Images.uploadImage(req));
    ab.add(POST, "/.*", req -> {
      Map<String, Object> m;
      m = Json.parseObject.apply(Strings.iter(new String(req.data)));
      return respond(new String(req.data));
    });
    return ab.build();
  }
}
