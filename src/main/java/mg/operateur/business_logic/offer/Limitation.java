/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mg.operateur.business_logic.offer;

/**
 *
 * @author dodaa
 */
public class Limitation {
    private int buyingLimit;
    private int durationInDays;

    public Limitation() {
    }

    public Limitation(int buyLimit, int durationInDays) throws Exception {
        setBuyingLimit(buyLimit);
        setDurationInDays(durationInDays);
    }

    public int getBuyingLimit() {
        return buyingLimit;
    }

    public void setBuyingLimit(int BuyLimit) throws Exception {
        if (BuyLimit < 0)
            throw new Exception("buying limit must not be negative");
        this.buyingLimit = BuyLimit;
    }

    public int getDurationInDays() {
        return durationInDays;
    }

    public void setDurationInDays(int durationInDays) throws Exception {
        if (durationInDays < 0)
            throw new Exception("duration must not be negative");
        this.durationInDays = durationInDays;
    }

    @Override
    public String toString() {
        return "Limitation{" + "\nbuyingLimit=" + buyingLimit + ", \ndurationInDays=" + durationInDays + '}';
    }
}
