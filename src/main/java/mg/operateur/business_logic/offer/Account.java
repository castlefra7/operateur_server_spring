/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mg.operateur.business_logic.offer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author dodaa
 */
public class Account {
    
    private int customerId;
    private List<Purchase> purchases;
    private List<Consumption> consumptions;

    public Account(int customerId, List<Purchase> purchases, List<Consumption> consumptions) throws Exception {
        setCustomerId(customerId);
        setPurchases(purchases);
        setConsumptions(consumptions);
    }
    
    public List<Consumption> getConsumptions() {
        return consumptions;
    }

    public void setConsumptions(List<Consumption> consumptions) throws Exception {
        if (consumptions == null)
            throw new Exception("consumptions are required");
        this.consumptions = consumptions;
    }
    
    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public List<Purchase> getPurchases() {
        return purchases;
    }

    public void setPurchases(List<Purchase> purchases) throws Exception {
        if (purchases == null)
            throw new Exception("purchases are required");
        this.purchases = purchases;
    }
    
    public List<Purchase> getValidPurchasesAtDate(SmartDate date) {
        List<Purchase> validPurchases = new ArrayList<>();
        for (int i = 0; i < purchases.size(); i++) {
            if (purchases.get(i).isStillValidAt(date)) {
                validPurchases.add(purchases.get(i));
            }
        }
        return validPurchases;
    }
    
    public List<Consumption> getValidConsumptionsFromDate(SmartDate date) {
        List<Consumption> validConsumptions = new ArrayList<>();
        for (int i = 0; i < consumptions.size(); i++) {
            if (consumptions.get(i).getDate().after(date)) {
                validConsumptions.add(consumptions.get(i));
            }
        }
        return validConsumptions;
    }
    
    public Offer getMatchingOfferAt(SmartDate date, Application app) throws Exception {
        List<Offer> total = getPurchasesTotalAt(date);
        for (int i = 0; i < total.size(); i++) {
            for (int j = 0; j < total.get(i).getAmounts().size(); j++) {
                 if (total.get(i).getAmounts().get(j).getApplication().equals(app)) {
                    return total.get(i);
                } 
            }
        }
        return null;
    }
    
    public List<Offer> getPurchasesTotalAt(SmartDate date) throws Exception {
        
        List<Purchase> validPurchases = getValidPurchasesAtDate(date);
        Map<String, Offer> offersMap = new HashMap<>(); 
        
        for (int i = 0; i < validPurchases.size(); i++) {
            String key = validPurchases.get(i).getOffer().getName();
            if (!offersMap.containsKey(key)) {
                Offer offer = validPurchases.get(i).getOffer();
                ArrayList<Amount> amounts = new ArrayList<>();
                for (Amount amount : offer.getAmounts()) {
                    Amount a = new Amount(amount);
                    a.setValue(0);
                    amounts.add(a);
                }
                Offer newOffer = new Offer(offer.getId(), offer.getName(), offer.getCreatedAt(), offer.getPrice(), offer.getValidityDay(), offer.getLimitation(), amounts);
                offersMap.put(key, newOffer);
            }
        }
        
        for (Map.Entry mapElement : offersMap.entrySet()) {
            String key = (String)mapElement.getKey();
            for (int i = 0; i < validPurchases.size(); i++) {
                if (validPurchases.get(i).getOffer().getName().equals(key)) {
                    offersMap.put(key, offersMap.get(key).cumulate(purchases.get(i).getOffer()));
                }
            }
        }
        
        List<Offer> val = new ArrayList<>();
        for (Map.Entry mapElement : offersMap.entrySet()) { 
            String key = (String)mapElement.getKey();
            val.add(offersMap.get(key));
        }
        
        return val;
    }
}
