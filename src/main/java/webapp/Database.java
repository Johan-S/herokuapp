/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package webapp;
import static httpserver.sql.ConnectionPool.*;
import java.net.URI;
/**
 *
 * @author johan
 */
public class Database {
    
  public static void init() throws Exception {
    System.out.println("Parsing db url: " + System.getenv("DATABASE_URL"));
    URI dbUri = new URI(System.getenv("DATABASE_URL"));
    String username = dbUri.getUserInfo().split(":")[0];
    String password = dbUri.getUserInfo().split(":")[1];
    String dbUrl = "jdbc:postgresql://" + dbUri.getHost() + ':' + dbUri.getPort() + dbUri.getPath();
    setUrl(dbUrl);
    setUsername(username);
    setPassword(password);
    
    execute("CREATE TABLE IF NOT EXISTS account ("
            + "name varchar(255) UNIQUE NOT NULL,"
            + "password_hash varchar(255) NOT NULL,"
            + "id SERIAL NOT NULL PRIMARY KEY"
            + ");");
    execute("CREATE TABLE IF NOT EXISTS session ("
            + "id varchar(50) NOT NULL PRIMARY KEY,"
            + "account_id int REFERENCES account(id)"
            + ");");
  }
}
