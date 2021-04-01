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
import mg.operateur.gen.InvalidAmountException;

/**
 *
 * @author lacha
 */
public class Pricing extends BaseClass {
    
    private Date created_at;
    private double amount_interior;
    private double amount_exterior;

    public Date getCreated_at() {
        return created_at;
    }

    public void setCreated_at(Date created_at) {
        this.created_at = created_at;
    }

    public double getAmount_interior() {
        return amount_interior;
    }

    public void setAmount_interior(double amount_interior) throws InvalidAmountException {
        if (amount_interior < 0)
            throw new InvalidAmountException("veuillez entrer un prix d'appel interne positif");
        this.amount_interior = amount_interior;
    }

    public double getAmount_exterior() {
        return amount_exterior;
    }

    public void setAmount_exterior(double amount_exterior) throws InvalidAmountException {
        if (amount_exterior < 0)
            throw new InvalidAmountException("veuillez entrer un prix d'appel externe positif");
        this.amount_exterior = amount_exterior;
    }
    
    public Pricing getLastPricing(Date _date, Connection conn) throws SQLException, InstantiationException, NoSuchMethodException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        String req = String.format("select * from %s order by created_at desc limit 1", tableName());
        Object ob = FctGen.find(this, req, columns(), conn);
        if(ob ==null) throw new NullPointerException("Pas encore de prix pour cette opération");
        return (Pricing)ob;
    }

    @Override
    public String tableName() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String[] columns() {
        return new String[]{"amount_interior", "amount_exterior", "created_at"};
    }
    
}
