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
public class Purchase {
    private int id;
    private int customerId;
    private SmartDate purchaseDate;
    private Offer offer;

    public Purchase(int customerId, Offer purchasedOffer, SmartDate purchaseDate) throws Exception {
        setCustomerId(customerId);
        setOffer(purchasedOffer);
        setPurchaseDate(purchaseDate);
    }

    public void setPurchaseDate(SmartDate purchaseDate) throws Exception {
        if (purchaseDate == null)
            throw new Exception("purchase date is required");
        this.purchaseDate = purchaseDate;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }
    
    public void setOffer(Offer offer) throws Exception {
        if (offer == null)
            throw new Exception("purchased offer is required");
        this.offer = offer;
    }

    public int getId() { return id; }
    public int getCustomerId() { return customerId; }
    public Offer getOffer() { return offer; }
    public SmartDate getPurchaseDate() { return purchaseDate; }

    public boolean isStillValidAt(SmartDate newPurchaseDate) {
        int validityDay = offer.getValidityDay();
        SmartDate addDays = purchaseDate.addDays(validityDay);
        return newPurchaseDate.before(addDays);
    }

    @Override
    public String toString() {
        return "OfferPurchase{" + "\t\nid=" + id + ", \t\ncustomerId=" + customerId + ", \t\npurchaseDate=" + purchaseDate + ", \t\noffer=" + offer + '}';
    }
}
