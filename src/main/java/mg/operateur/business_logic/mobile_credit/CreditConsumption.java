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
public class CreditConsumption extends Transaction {

    @Override
    public void insert(Connection conn) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, SQLException {
        FctGen.insert(this, columns(), tableName(), conn);
    }

    @Override
    public String tableName() {
        return "mg.credit_consumptions";
    }

    @Override
    public String[] columns() {
        return new String[]{"created_at", "customer_id", "cons_amount"};
    }
    
    /* PROPERTIES */
    
    private double cons_amount;

    public double getCons_amount() {
        return cons_amount;
    }

    public void setCons_amount(double cons_amount) {
        this.cons_amount = cons_amount;
    }
    
    
}
