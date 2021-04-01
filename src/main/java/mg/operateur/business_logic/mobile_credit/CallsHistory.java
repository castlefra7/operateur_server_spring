/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mg.operateur.business_logic.mobile_credit;

import java.lang.reflect.InvocationTargetException;
import java.net.URISyntaxException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import mg.operateur.conn.ConnGen;
import mg.operateur.gen.FctGen;

/**
 *
 * @author lacha
 */
public class CallsHistory extends BaseClass {
    private Date created_at;
    private int amount;
    private String phone_number;

    public Date getCreated_at() {
        return created_at;
    }

    public void setCreated_at(Date created_at) {
        this.created_at = created_at;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }
     private void out(Exception ex) {
        ex.printStackTrace();
        System.out.println(ex.getMessage());
    }
    public List<Object> findAll(int _customer_id) throws SQLException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, URISyntaxException {
        Connection conn = null;
        List<Object> result = new ArrayList();
        try {
            conn = ConnGen.getConn();
            result = findAll(_customer_id, conn);
        } catch(IllegalAccessException | IllegalArgumentException | InstantiationException | NoSuchMethodException | InvocationTargetException | URISyntaxException | SQLException ex) {
            throw ex;
        }  finally {
            try {if(conn!=null) conn.close();}catch(SQLException ex) {out(ex);}
        }
        return result;
    }
    
    public List<Object> findAll(int _customer_id, Connection conn) throws SQLException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException {
        return FctGen.findAll(new CallsHistory(), String.format("select *from mg.calls_history where customer_id= %d", _customer_id), columns(), conn);
    }

    @Override
    public String tableName() {
        return "mg.calls_history";
    }

    @Override
    public String[] columns() {
        return new String[] {"created_at", "amount", "phone_number"};
    }
}
