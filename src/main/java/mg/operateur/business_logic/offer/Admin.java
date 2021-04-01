/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mg.operateur.business_logic.offer;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URISyntaxException;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.Date;
import mg.operateur.conn.ConnGen;
import mg.operateur.gen.CDate;
import mg.operateur.gen.FctGen;
import mg.operateur.web_services.resources.offer.CustomerJSON;

/**
 *
 * @author dodaa
 */
public final class Admin {

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

    public Admin FindByName(String name)
            throws SQLException, InstantiationException, NoSuchMethodException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, URISyntaxException, IOException {
        Connection conn = null;
        Admin result = null;
        try {
            conn = ConnGen.getConn();
            result = FindByName(name, conn);
        } catch (IllegalAccessException | IllegalArgumentException | InstantiationException | NoSuchMethodException | InvocationTargetException | URISyntaxException | SQLException ex) {
            throw ex;
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException ex) {
                System.out.println(ex);
            }
        }
        return result;
    }

    public Admin FindByName(String name, Connection conn)
            throws SQLException, InstantiationException, NoSuchMethodException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        PreparedStatement pst = null;
        try {
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
        } catch (Exception ex) {
            throw ex;
        } finally {
            if (pst != null) {
                pst.close();
            }
        }
    }

    public Admin insert(CustomerJSON _customer)
            throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, SQLException, URISyntaxException, NoSuchAlgorithmException, ParseException, IOException {

        Connection conn = null;
        Admin result = null;
        try {
            conn = ConnGen.getConn();
            Admin admin = new Admin();
            admin.setName(_customer.getName());
            admin.setPwd(PasswordHelper.md5(_customer.getPassword()));
            admin.setCreated_at(CDate.getDate().parse(_customer.getCreatedAt()));
            admin.insert(conn);
            result = admin;
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | URISyntaxException | NoSuchAlgorithmException | SQLException | ParseException ex) {
            throw ex;
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException ex) {
                System.out.println(ex);
            }
        }
        return result;
    }

    public void insert(Connection conn)
            throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, SQLException {
        FctGen.insert(this, columns(), "mg.users", conn);
    }

    public String[] columns() {
        return new String[]{"name", "pwd", "created_at"};
    }
}
