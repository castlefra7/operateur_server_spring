/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mg.operateur.business_logic.offer;

import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import mg.operateur.gen.FctGen;

/**
 *
 * @author dodaa
 */
public class Admin {
    private String name;
    private String pwd;
    private Date created_at;

    public Admin() {
    }

    public Admin(String name, String password, Date created_at) {
        setName(name);
        setPwd(password);
        setCreated_at(created_at);
    }

    public Date getCreated_at() {
        return created_at;
    }

    public void setCreated_at(Date created_at) {
        this.created_at = created_at;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }
    
    public Admin FindByName(String name, Connection conn) 
            throws SQLException, InstantiationException, NoSuchMethodException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        PreparedStatement pst = null;
        try{
            pst = conn.prepareStatement("SELECT * FROM mg.users WHERE name = ?");
            pst.setString(1, name);
            ResultSet res = pst.executeQuery();
            
            Admin admin = null;
            while (res.next()) {
                
                admin = new Admin(
                        res.getString("name"),
                        res.getString("pwd"),
                        res.getDate("created_at")
                    );
            }
            return admin;
        }catch(Exception ex){   
            throw ex;
        }finally{
            if(pst!=null)pst.close();
        }
    }
    
    public void insert(Connection conn) 
            throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, SQLException {
        FctGen.insert(this, columns(), "mg.users", conn);
    }
    
    public String[] columns() {
        return new String[] {"name", "pwd", "created_at"};
    }
}
