/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mg.operateur.conn;

import java.net.URI;
import java.net.URISyntaxException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 *
 * @author lacha
 */
public class ConnGen {
    public ConnGen() throws ClassNotFoundException {
        Class.forName("org.postgresql.Driver");
    }
    
    public static Connection getConn() throws SQLException, URISyntaxException {
        Connection conn = null;
        if (System.getenv("DATABASE_URL") != null) {
            
            URI dbUri = new URI(System.getenv("DATABASE_URL"));

            String username = dbUri.getUserInfo().split(":")[0];
            String password = dbUri.getUserInfo().split(":")[1];
            String dbUrl = "jdbc:postgresql://" + dbUri.getHost() + ':' + dbUri.getPort() + dbUri.getPath() + "?sslmode=require";

            conn = DriverManager.getConnection(dbUrl, username, password);
        } else {
            String dbUrl = "jdbc:postgresql://" + "localhost" + ':' + "5432" + "/db_operateur";
            conn = DriverManager.getConnection(dbUrl, "operateur", "123456");
        }
        return conn;
    }
}
