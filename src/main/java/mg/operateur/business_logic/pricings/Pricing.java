/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mg.operateur.business_logic.pricings;

import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.SQLException;
import mg.operateur.business_logic.mobile_credit.Transaction;
import mg.operateur.gen.FctGen;

/**
 *
 * @author lacha
 */
public class Pricing extends Transaction {

    private int application_id;
    private double amount_interior;
    private double amount_exterior;
    
    
    @Override
    public void insert(Connection conn) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, SQLException {
        FctGen.insert(this, columns(), tableName(), conn);
    }

    @Override
    public String tableName() {
        return "mg.pricings";
    }

    @Override
    public String[] columns() {
        return new String[]{"created_at", "application_id", "amount_interior", "amount_exterior"};
    }
    
    /* PROPERTIES */

    public int getApplication_id() {
        return application_id;
    }

    public void setApplication_id(int application_id) {
        this.application_id = application_id;
    }

    public double getAmount_interior() {
        return amount_interior;
    }

    public void setAmount_interior(double amount_interior) {
        this.amount_interior = amount_interior;
    }

    public double getAmount_exterior() {
        return amount_exterior;
    }

    public void setAmount_exterior(double amount_exterior) {
        this.amount_exterior = amount_exterior;
    }
    
    
}
