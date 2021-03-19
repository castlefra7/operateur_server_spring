/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mg.operateur.business_logic.mobile_credit;

import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.SQLException;
import mg.operateur.gen.FctGen;

/**
 *
 * @author lacha
 */
public class CallPricing extends Pricing {
    
    public void insert(Connection conn) 
            throws IllegalAccessException, IllegalAccessException, InvocationTargetException, SQLException {
        FctGen.insert(this, columns(), tableName(), conn);
    }
    
    @Override
    public String tableName() {
        return "mg.calls_pricings";
    }
}