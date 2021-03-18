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
public class MessageCallConsumption extends BaseClass {
    private Date created_at;
    private int customer_id;
    private int customer_destination_id;
    private int amount;
    private char t_type;

    public char getT_type() {
        return t_type;
    }

    public void setT_type(char t_type) {
        this.t_type = t_type;
    }
    
    public Date getCreated_at() {
        return created_at;
    }

    public void setCreated_at(Date created_at) {
        this.created_at = created_at;
    }

    public int getCustomer_id() {
        return customer_id;
    }

    public void setCustomer_id(int customer_id) {
        this.customer_id = customer_id;
    }

    public int getCustomer_destination_id() {
        return customer_destination_id;
    }

    public void setCustomer_destination_id(int customer_destination_id) {
        this.customer_destination_id = customer_destination_id;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) throws InvalidAmountException {
        if(amount <= 0) throw new InvalidAmountException("Le nombre que vous avez entrer est incorrect");
        this.amount = amount;
    }
    
    public void insert(Connection conn) throws IllegalAccessException, InvocationTargetException, IllegalArgumentException, SQLException {
        FctGen.insert(this, columns(), tableName(), conn);
    }
    
    @Override
    public String tableName() {
        return "mg.messages_calls_consumptions";
    }

    @Override
    public String[] columns() {
        return new String[]{"created_at", "customer_id", "customer_destination_id", "amount", "t_type"};
    }
    
}
