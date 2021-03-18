/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mg.operateur.business_logic.mobile_credit;

import mg.operateur.gen.FctGen;
import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.SQLException;

/**
 *
 * @author lacha
 */
public class Deposit extends Transaction{

    public Deposit(int _id) {
        this.setId(_id);
    }
    
    public Deposit() {}
    
    @Override
    public void insert(Connection conn) throws IllegalAccessException, InvocationTargetException, IllegalArgumentException, SQLException {
        FctGen.insert(this, columns(), tableName(), conn);
    }
    

    
    public void validate(Connection conn) throws SQLException {
        String req = String.format("update mg.deposits set isvalidated = true where id =%d", this.getId());
        FctGen.update(req, conn);
    }
    
    @Override
    public String tableName() {
        return "mg.deposits";
    }

    @Override
    public String[] columns() {
        return new String[]{"created_at", "customer_id", "amount", "customer_source_id"};
    }
    

    
    /* PROPERTIES */
    private int customer_source_id;

    public int getCustomer_source_id() {
        return customer_source_id;
    }

    public void setCustomer_source_id(int customer_source_id) {
        this.customer_source_id = customer_source_id;
    }
    
}
