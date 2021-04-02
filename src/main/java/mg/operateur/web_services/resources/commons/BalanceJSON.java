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
    private String remainCalIntMess;
    private String remainOffers;

    public String getRemainCalIntMess() {
        return remainCalIntMess;
    }

    public void setRemainCalIntMess(String remainCalIntMess) {
        this.remainCalIntMess = remainCalIntMess;
    }

    public String getRemainOffers() {
        return remainOffers;
    }

    public void setRemainOffers(String remainOffers) {
        this.remainOffers = remainOffers;
    }
    
    
    
    public BalanceJSON(double _credit, double _money, String _remainCall, String _remainOffer) {
        this.setCreditBalance(_credit);
        this.setMobileMoneyBalance(_money);
        this.setRemainCalIntMess(_remainCall);
        this.setRemainOffers(_remainOffer);
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
