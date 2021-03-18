/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mg.operateur.business_logic.offer;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
/**
 *
 * @author dodaa
 */
public class Customer {
    private int id;
    private String name;
    private String email;
    private PhoneNumber phoneNumber;
    private Account account;

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public Customer() {
    }

    public Customer(int id, String name, String email, PhoneNumber phoneNumber) throws Exception {
        setId(id);
        setName(name);
        setEmail(email);
        setPhoneNumber(phoneNumber);
        account = new Account(id, new ArrayList<>(), new ArrayList<>());
    }
    
    public Customer(String name, String email, PhoneNumber phoneNumber) throws Exception {
        setName(name);
        setEmail(email);
        setPhoneNumber(phoneNumber);
        account = new Account(id, new ArrayList<>(), new ArrayList<>());
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) throws Exception { 
        if (name == null)
            throw new Exception("customer name required");
        this.name = name;
    }
    
    public void setEmail(String email) throws Exception { 
        if (email == null)
            throw new Exception("email is required");
        this.email = email; 
    }
    public void setPhoneNumber(PhoneNumber phoneNumber) throws Exception { 
        if (phoneNumber == null)
            throw new Exception("phonenumber is required");
        this.phoneNumber = phoneNumber; 
    }
   
    public int getId() { return id; }
    public String getName() { return name; }
    public String getEmail() { return email; }
    public PhoneNumber getPhoneNumber() { return phoneNumber; }
    
    public void purchase (Offer offer, Date newPurchaseDate) throws Exception {
        
        int buyingLimit = offer.getLimitation().getBuyingLimit();
        int buys = 1;
        
        List<Purchase> validPurchase = account.getValidPurchasesAtDate(newPurchaseDate);
        for (Purchase purchase : validPurchase) {
            if (purchase.getOffer().equals(offer)) {
                buys ++;
                if (buys > buyingLimit) 
                    throw new Exception("La limite d'achat du forfait " + offer.getName() + " est atteinte");
            }
        }
        account.getPurchases().add(new Purchase(id, offer, newPurchaseDate, offer.getId()));
    }
    
    public void purchase (Offer offer, Date newPurchaseDate, Connection conn) throws Exception {
        
        int buyingLimit = offer.getLimitation().getBuyingLimit();
        int buys = 1;
        
        List<Purchase> validPurchase = account.getValidPurchasesAtDate(newPurchaseDate);
        for (Purchase purchase : validPurchase) {
            if (purchase.getOffer().equals(offer)) {
                buys ++;
                if (buys > buyingLimit) 
                    throw new Exception("La limite d'achat du forfait " + offer.getName() + " est atteinte");
            }
        }
        new Purchase(id, offer, newPurchaseDate, offer.getId()).save(conn);
    }
    
    public void consume(Application app, Amount consumed, SmartDate consumptionDate) throws Exception {
        if (!app.equals(consumed.getApplication()))
                throw new Exception("Application and consumption does not match");
        
        account.getConsumptions().add(new Consumption(1, id, app, consumptionDate, consumed));
    }
}
