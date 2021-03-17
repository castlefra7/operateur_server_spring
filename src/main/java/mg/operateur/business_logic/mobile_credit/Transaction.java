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

/**
 *
 * @author lacha
 */
public abstract class Transaction extends BaseClass {
    private Date created_at;
    private int customer_id;
    private double amount;

    public abstract void insert(Connection conn)  throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, SQLException;
    
    public Date getCreated_at() {
        return created_at;
    }

    public void setCreated_at(Date created_at) throws Exception {
        if(created_at == null) throw new Exception("Veuillez entrer une date valide");
        this.created_at = created_at;
    }

    public int getCustomer_id() {
        return customer_id;
    }

    public void setCustomer_id(int customer_id) {
        this.customer_id = customer_id;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) throws Exception {
        if(amount <= 0) throw new Exception("Veuillez entrer un montant correct");
        this.amount = amount;
    }
    
}
