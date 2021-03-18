/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mg.operateur.web_services.resources.pricings;

import mg.operateur.gen.InvalidAmountException;

/**
 *
 * @author lacha
 */
public class PricingJSON {
    private String date;
    private int application_id;
    private double amount_interior;
    private double amount_exterior;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getApplication_id() {
        return application_id;
    }

    public void setApplication_id(int application_id) {
        this.application_id = application_id;
    }
    
    public double getAmount_interior() {
        return amount_interior;
    }

    public void setAmount_interior(double amount_interior) {
        this.amount_interior = amount_interior;
    }

    public double getAmount_exterior() {
        return amount_exterior;
    }

    public void setAmount_exterior(double amount_exterior) {
        this.amount_exterior = amount_exterior;
    }
    
}
