
package webapp;
import httpserver.sql.ConnectionPool;
import util.*;


public abstract class Account {
  private static final String SQL_REGISTER = "INSERT INTO account (name, password_hash) VALUES (%s, %s) RETURNING id;";
  private static final String SQL_LOGIN = "SELECT id FROM account WHERE name = %s AND password_hash = %s;";
  
  public static Long create(String name, String password) {
    password = Security.hashPassword(password);
    return ConnectionPool.query(Strings.formatSQL(SQL_REGISTER, name, password), 
            res -> {
              if (res.isLast())
                return -1l;
              res.next();
      return res.getLong(1);
    });
  }
  
  
  public static Long login(String name, String password) {
    password = Security.hashPassword(password);
    return ConnectionPool.query(Strings.formatSQL(SQL_LOGIN, name, password), 
            res -> {
              if (res.isLast())
                return -1l;
              res.next();
               return res.getLong(1);
    });
  }
  
}
