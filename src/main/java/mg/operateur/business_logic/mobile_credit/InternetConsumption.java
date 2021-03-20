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
 * @author lacha
 */
public class InternetConsumption extends BaseClass {
    private Date created_at;
    private int customer_id;
    private double amount;
    private int internet_application_id;

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

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public int getInternet_application_id() {
        return internet_application_id;
    }

    public void setInternet_application_id(int internet_application_id) {
        this.internet_application_id = internet_application_id;
    }
    
    public void insert(Connection conn) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, SQLException {
        FctGen.insert(this, columns(), tableName(), conn);
    }

    @Override
    public String tableName() {
        return "mg.internet_consumptions";
    }

    @Override
    public String[] columns() {
       return new String[]{"created_at", "customer_id", "amount", "internet_application_id" };
    }
}
