


package webapp;

import httpserver.*;
import httpserver.sql.ConnectionPool;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.NumberFormat;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;


public class Webapp {
  
  public static String vars() {
    Runtime runtime = Runtime.getRuntime();

    NumberFormat format = NumberFormat.getInstance();

    StringBuilder sb = new StringBuilder();
    long maxMemory = runtime.maxMemory();
    long allocatedMemory = runtime.totalMemory();
    long freeMemory = runtime.freeMemory();

    sb.append("free memory: ").append(format.format(freeMemory / 1024)).append("k\n");
    sb.append("allocated memory: ").append(format.format(allocatedMemory / 1024)).append("k\n");
    sb.append("max memory: ").append(format.format(maxMemory / 1024)).append("k\n");
    sb.append("total free memory: ").append(format.format((freeMemory + (maxMemory - allocatedMemory)) / 1024)).append("k\n");
    return sb.toString();
  }

  public static void initLog() throws IOException {
    Logger.getLogger(Server.class.getName()).setLevel(Level.FINE);
    
    final FileHandler fh = new FileHandler("logs/info");
    fh.setFormatter(new SimpleFormatter());
    fh.setLevel(Level.INFO);
    Logger.getGlobal().getParent().addHandler(fh);
    final FileHandler fh2 = new FileHandler("logs/verbose");
    fh2.setFormatter(new SimpleFormatter());
    fh2.setLevel(Level.FINE);
    Logger.getGlobal().getParent().addHandler(fh2);
    
  }
  
  public static void runServers() throws IOException {
    int port = Integer.valueOf(System.getenv("PORT"));
    Server s = Server.create(port);
    s.start(Router.get());
    System.out.println(vars());
  }
  
  public static void main(String[] args) throws Exception {
    Database.init();
    initLog();
    runServers();
  }
}
