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
public class MessagePricing extends Pricing {
    
    private int unit;

    public int getUnit() {
        return unit;
    }

    public void setUnit(int unit) throws Exception {
        if (unit < 0)
            throw new Exception("Veuillez entrer une unité de message positive");
        this.unit = unit;
    }
    
    public void insert(Connection conn) 
            throws IllegalAccessException, IllegalArgumentException, SQLException, InvocationTargetException {
        FctGen.insert(this, columns(), tableName(), conn);
    }
    
    @Override
    public String tableName() {
        return "mg.messages_pricings";
    }
    
    @Override
    public String[] columns() {
        return new String[]{"amount_interior", "amount_exterior", "created_at", "unit"};
    }
}
