/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mg.operateur.web_services.resources.mobilemoney;

/**
 *
 * @author lacha
 */
public class FeeJSON {
    private String date;
    private double amount_min;
    private double amount_max;
    private double amount_fee;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public double getAmount_min() {
        return amount_min;
    }

    public void setAmount_min(double amount_min) {
        this.amount_min = amount_min;
    }

    public double getAmount_max() {
        return amount_max;
    }

    public void setAmount_max(double amount_max) {
        this.amount_max = amount_max;
    }

    public double getAmount_fee() {
        return amount_fee;
    }

    public void setAmount_fee(double amount_fee) {
        this.amount_fee = amount_fee;
    }
    
    
}
