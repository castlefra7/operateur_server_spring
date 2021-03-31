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
    
    public int convertToSec(String amount) {
        amount = amount.toLowerCase();
        int val = 0;
        return val;
    }
    
    public void insert(Connection conn) 
            throws IllegalAccessException, IllegalAccessException, InvocationTargetException, SQLException {
        FctGen.insert(this, columns(), tableName(), conn);
    }
    
    public CallPricing findLatest(Connection conn) 
            throws SQLException, InstantiationException, NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        return (CallPricing) FctGen.find(new CallPricing(), "select * from mg.calls_pricings where created_at in (select max(created_at) from mg.calls_pricings)", new String[] {"created_at", "amount_interior", "amount_exterior", "unit" }, conn);
    }
    
    @Override
    public String tableName() {
        return "mg.calls_pricings";
    }
}