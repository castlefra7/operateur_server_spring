/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mg.operateur.business_logic.pricings;

import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import mg.operateur.business_logic.mobile_credit.Transaction;
import mg.operateur.business_logic.offer.Offer;
import mg.operateur.business_logic.offer.Purchase;
import mg.operateur.gen.FctGen;
import mg.operateur.gen.RequiredException;

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
    
    public void dropAll(Connection conn) throws SQLException, Exception {
        PreparedStatement pst = null;
        try{
            pst = conn.prepareStatement("DELETE FROM mg.pricings WHERE application_id = ?");
            pst.setInt(1, application_id);
            pst.executeUpdate();
        }catch(Exception ex){   
            throw ex;
        }finally{
            if(pst!=null)pst.close();
        }
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

    public void setAmount_interior(double amount_interior) throws RequiredException {
        if (amount_interior < 0)
            throw new RequiredException("Le montant extérieur doit-être posistif");
        this.amount_interior = amount_interior;
    }

    public double getAmount_exterior() {
        return amount_exterior;
    }

    public void setAmount_exterior(double amount_exterior) throws RequiredException {
        if (amount_exterior < 0)
            throw new RequiredException("Le montant extérieur doit-être posistif");
        this.amount_exterior = amount_exterior;
    }
    
    
}
