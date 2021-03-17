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
public class Credit extends Transaction {

    @Override
    public void insert(Connection conn)  throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, SQLException {
        FctGen.insert(this, columns(), tableName(), conn);
    }

    @Override
    public String tableName() {
        return "mg.buyed_credits";
    }

    @Override
    public String[] columns() {
        return new String[]{"created_at", "customer_id", "customer_source_id", "amount"};
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
