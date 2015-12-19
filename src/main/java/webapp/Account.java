
package webapp;
import httpserver.*;
import httpserver.sql.ConnectionPool;
import util.*;


public abstract class Account {
  private static final String createsql = "INSERT INTO account (name, password_hash) VALUES (%s, %s) RETURNING id;";
  
  public static Long create(String name, String password) {
    password = Security.hashPassword(password);
    return ConnectionPool.query(Strings.formatSQL(createsql, name, password), 
            res -> {
              res.next();
      return res.getLong(1);
    });
  }
  
  private static final String loginsql = "SELECT id FROM account WHERE name = %s AND password_hash = %s;";
  
  public static Long login(String name, String password) {
    password = Security.hashPassword(password);
    return ConnectionPool.query(Strings.formatSQL(loginsql, name, password), 
            res -> {
              res.next();
      return res.getLong(1);
    });
  }
  
}
