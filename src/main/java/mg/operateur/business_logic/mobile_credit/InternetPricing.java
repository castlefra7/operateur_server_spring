/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mg.operateur.business_logic.mobile_credit;

import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;
import mg.operateur.gen.FctGen;

/**
 *
 * @author dodaa
 */
public class InternetPricing {
    private Date created_at;
    private double amount;

    public Date getCreated_at() {
        return created_at;
    }

    public void setCreated_at(Date created_at) {
        this.created_at = created_at;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) throws Exception {
        if (amount < 0)
            throw new Exception("Veuillez entrer un prix d'internet Positif");
        this.amount = amount;
    }
    
    public void insert(Connection conn) 
            throws IllegalAccessException, InvocationTargetException, SQLException {
        FctGen.insert(this, columns(), tableName(), conn);
    }
    
    public String tableName() {
        return "mg.internet_pricings";
    }
    
    public String[] columns() {
        return new String[]{"created_at", "amount"};
    }
}
