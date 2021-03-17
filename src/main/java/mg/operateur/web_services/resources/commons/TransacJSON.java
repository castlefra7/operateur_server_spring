/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mg.operateur.web_services.resources.commons;

/**
 *
 * @author lacha
 */
public class TransacJSON {
    private String date;
    private double amount;
    private String phone_number;

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }
    
    
    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }
    
     
    @Override
    public String toString() {
        String result;
        result = String.format("{%f; %s; %s}", getAmount(), getPhone_number(), getDate());
        return result;
    }
}
