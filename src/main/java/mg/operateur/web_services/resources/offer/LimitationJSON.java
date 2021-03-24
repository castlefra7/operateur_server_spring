/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mg.operateur.web_services.resources.offer;

/**
 *
 * @author dodaa
 */
public class LimitationJSON {
    private int buyingLimit;
    private int durationInDays;

    public int getBuyingLimit() {
        return buyingLimit;
    }

    public void setBuyingLimit(int BuyLimit) throws Exception {
        this.buyingLimit = BuyLimit;
    }

    public int getDurationInDays() {
        return durationInDays;
    }

    public void setDurationInDays(int durationInDays) throws Exception {
        this.durationInDays = durationInDays;
    }
}
