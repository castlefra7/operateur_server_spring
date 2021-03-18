/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mg.operateur.business_logic.offer;

import java.lang.reflect.InvocationTargetException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import mg.operateur.gen.FctGen;
/**
 *
 * @author dodaa
 */
public class Customer {
    private int id;
    private String name;
    private String email;
    private String phone_number;
    private Account account;
    private String password;
    private Date created_at;

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public Customer() {
    }

    public Customer(int id, String name, String email, String phoneNumber) throws Exception {
        setId(id);
        setName(name);
        setEmail(email);
        setPhone_number(phoneNumber);
        account = new Account(id, new ArrayList<>(), new ArrayList<>());
    }
    
    public Customer(String name, String email, String phoneNumber) throws Exception {
        setName(name);
        setEmail(email);
        setPhone_number(phoneNumber);
        account = new Account(id, new ArrayList<>(), new ArrayList<>());
    }
    
    public Customer(String name, String email, String password, Date createdAt, String phoneNumber) throws Exception {
        setName(name);
        setEmail(email);
        setPhone_number(phoneNumber);
        setPassword(password);
        setCreated_at(createdAt);
        account = new Account(id, new ArrayList<>(), new ArrayList<>());
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) throws NoSuchAlgorithmException {
        this.password = PasswordHelper.md5(password);
    }

    public Date getCreated_at() {
        return created_at;
    }

    public void setCreated_at(Date created_at) {
        this.created_at = created_at;
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
    public void setPhone_number(String phone_number) throws Exception { 
        if (phone_number == null)
            throw new Exception("phonenumber is required");
        this.phone_number = phone_number; 
    }
   
    public int getId() { return id; }
    public String getName() { return name; }
    public String getEmail() { return email; }
    public String getPhone_number() { return phone_number; }
    
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
    
    public void save(Connection conn) throws IllegalAccessException, InvocationTargetException, SQLException {
        FctGen.insert(this, new String[] { "created_at", "name", "email", "phone_number", "password" }, "mg.customers", conn);
    }
}
