/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mg.operateur.conn;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

/**
 *
 * @author dodaa
 */
public class PropReader {

    private Properties prop;
    
    public PropReader() throws FileNotFoundException, IOException {
        initReader();
    }
    
    private void initReader() throws FileNotFoundException, IOException {
        String rootPath = Thread.currentThread().getContextClassLoader().getResource("").getPath();
        String appConfigPath = rootPath + "db.properties";
        prop = new Properties();
        prop.load(new FileInputStream(appConfigPath));
    }
    
    public String getDbUser() {
        return prop.getProperty("db_user");
    }
    
    public String getDbName() {
        return prop.getProperty("db_name");
    }
    
    public String getDbPort() {
        return prop.getProperty("db_port");
    }
    
    public String getDbPassword() {
        return prop.getProperty("db_password");
    }
}
