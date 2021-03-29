/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mg.operateur.web_services.resources.commons;

import java.util.HashMap;

/**
 *
 * @author lacha
 */
public final class BalanceJSON {
    private double creditBalance;
    private double mobileMoneyBalance;
    private HashMap<String, Double> remainingOffers;
    
    public BalanceJSON(double _credit, double _money, HashMap<String, Double> _remain) {
        this.setCreditBalance(_credit);
        this.setMobileMoneyBalance(_money);
        this.setRemainingOffers(_remain);
    }

    public double getCreditBalance() {
        return creditBalance;
    }

    public void setCreditBalance(double creditBalance) {
        this.creditBalance = creditBalance;
    }

    public double getMobileMoneyBalance() {
        return mobileMoneyBalance;
    }

    public void setMobileMoneyBalance(double mobileMoneyBalance) {
        this.mobileMoneyBalance = mobileMoneyBalance;
    }

    public HashMap<String, Double> getRemainingOffers() {
        return remainingOffers;
    }

    public void setRemainingOffers(HashMap<String, Double> remainingOffers) {
        this.remainingOffers = remainingOffers;
    }
    
    
}
