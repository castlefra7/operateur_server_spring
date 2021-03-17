/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mg.operateur.conn;

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
    
    public static Connection getConn() throws SQLException {
        String url = "jdbc:postgresql://localhost/db_operateur";
        Properties props = new Properties();
        props.setProperty("user","operateur");
        props.setProperty("password","123456");
        //props.setProperty("ssl","true");
        Connection conn = DriverManager.getConnection(url, props);
        return conn;
    }
}
