/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mg.operateur.business_logic.offer;

import java.lang.reflect.InvocationTargetException;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import mg.operateur.business_logic.mobile_credit.CreditConsumption;
import mg.operateur.business_logic.mobile_credit.Withdraw;
import mg.operateur.gen.CDate;
import mg.operateur.gen.FctGen;
import mg.operateur.gen.InvalidAmountException;
import mg.operateur.gen.InvalidDateException;
import mg.operateur.gen.InvalidFormatException;
import mg.operateur.gen.LimitReachedException;
import mg.operateur.gen.NotFoundException;
import mg.operateur.gen.RequiredException;
import mg.operateur.web_services.controllers.OfferRepository;
import mg.operateur.web_services.controllers.PurchaseController;
import mg.operateur.web_services.controllers.PurchaseRepository;
import mg.operateur.web_services.resources.commons.TransacJSON;

import org.springframework.data.annotation.Id;

/**
 *
 * @author dodaa
 */
public final class Offer {
    
    @Id
    private int id;
    private String name;
    private double price;
    private Date createdAt;
    private int validityDay;
    private Limitation limitation;
    private int priority;
    private List<Amount> amounts;
    
    private String code;

    public String getCode() {
        return code;
    }

    public void setCode(String code) throws Exception {
        if (code == null)
            throw new Exception("le code de l'offre est obligatoire");
        this.code = code;
    }
    
    private boolean isOneDay;

    public boolean getIsOneDay() {
        return isOneDay;
    }

    public void setIsOneDay(boolean isOneDay) {
        this.isOneDay = isOneDay;
    }
    
    

    public Offer() {
    }

    public Offer(int id, String name, Date createdAt, double price, int validityDay, Limitation limitation, List<Amount> amounts, int priority) throws Exception {
        setId(id);
        setName(name);
        setCreatedAt(createdAt);
        setPrice(price);
        setValidityDay(validityDay);
        setLimitation(limitation);
        setAmounts(amounts);
        setPriority(priority);
    }
    
    public Offer(String name, Date createdAt, double price, int validityDay, Limitation limitation, List<Amount> amounts, int priority) throws Exception {
        setName(name);
        setCreatedAt(createdAt);
        setPrice(price);
        setValidityDay(validityDay);
        setLimitation(limitation);
        setAmounts(amounts);
        setPriority(priority);
    }
    
    public void setId(int id) {
        this.id = id;
    }
    public void setName(String name) throws Exception { 
        if (name == null)
            throw new Exception("Offer name is required");
        this.name = name; 
    }
    public void setCreatedAt(Date createdAt) throws Exception {
        if (createdAt == null)
            throw new Exception("must enter offer date creation");
        this.createdAt = createdAt; 
    }
    public void setPrice(double price) throws Exception {
        if (price < 0) 
            throw new Exception("Price must not be negative");
        this.price = price; 
    }
    public void setValidityDay(int validityDay) throws Exception { 
        if (validityDay < 0)
            throw new Exception("duration must not be negative");
        this.validityDay = validityDay; 
    }
    public void setLimitation(Limitation limitation) throws Exception { 
        if (limitation == null)
            throw new Exception("must enter limitation for offer");
        this.limitation = limitation;
    }
    public void setAmounts(List<Amount> amounts) throws Exception {
        if (amounts == null)
            throw new Exception("amounts are required");
        if (amounts.isEmpty())
            throw new Exception("offer has a minimal of 1 amount");
        this.amounts = amounts;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }
    public int getId() { return id; }
    public int getPriority() { return priority; }
    public String getName() { return name; }
    public Date getCreatedAt() { return createdAt; }
    public double getPrice() { return price; }
    public int getValidityDay() { return validityDay; }
    public Limitation getLimitation() { return limitation; }
    public List<Amount> getAmounts() { return amounts; }
    
    public void buyFromMobileMoney(TransacJSON _purchase, OfferRepository offerRepository, PurchaseRepository purchaseRepository, Connection conn) throws NotFoundException, RequiredException, SQLException, InstantiationException, NoSuchMethodException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, ParseException, LimitReachedException, InvalidAmountException, InvalidDateException, InvalidFormatException, NoSuchAlgorithmException {
        mg.operateur.business_logic.mobile_credit.Customer foundCustomer = new mg.operateur.business_logic.mobile_credit.Customer().find(_purchase.getPhone_number(), conn);        
        foundCustomer.checkLastOperation(CDate.getDate().parse(_purchase.getDate()), conn);
        Offer offer = offerRepository.findByCode(_purchase.getCode());
       
        if (offer == null)
            throw new NotFoundException("L'offre specifié n'existe pas"); 
        if(offer.getPrice() > foundCustomer.mobileBalance(CDate.getDate().parse(_purchase.getDate()), conn)) throw new InvalidAmountException("Votre solde mobile money est insuffisant pour acheter cette offre");
        mg.operateur.business_logic.offer.Customer customer = new mg.operateur.business_logic.offer.
                Customer(foundCustomer.getId(), foundCustomer.getName(), foundCustomer.getEmail(), foundCustomer.getPhone_number());
        List<Purchase> purchases = purchaseRepository.findByCustomer_id(customer.getId());
        purchases.forEach(p -> {
            try {
                p.setOffer(offerRepository.findById(p.getOffer_id()));
            } catch (RequiredException ex) {
                Logger.getLogger(PurchaseController.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        
        Account account = new Account(customer.getId(), purchases, new ArrayList<>());
        customer.setAccount(account);
        
        
        Date date = CDate.getDate().parse(_purchase.getDate());
        
        Withdraw withdraw = new Withdraw();
        withdraw.setCreated_at(date);
        withdraw.setCustomer_id(foundCustomer.getId());
        withdraw.setAmount(offer.getPrice());
        foundCustomer.withdraw(withdraw, _purchase.getPassword(), true, conn);
        
        customer.purchase(offer, date, conn, purchaseRepository);
        
        
    }
    
    public void buy(TransacJSON _purchase, OfferRepository offerRepository, PurchaseRepository purchaseRepository, Connection conn) throws NotFoundException, RequiredException, SQLException, InstantiationException, NoSuchMethodException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, ParseException, LimitReachedException, InvalidAmountException, InvalidDateException, InvalidFormatException {
        
        mg.operateur.business_logic.mobile_credit.Customer foundCustomer = new mg.operateur.business_logic.mobile_credit.Customer().find(_purchase.getPhone_number(), conn);        
        foundCustomer.checkLastOperation(CDate.getDate().parse(_purchase.getDate()), conn);
        
        Offer offer = offerRepository.findByCode(_purchase.getCode());
        if (offer == null)
            throw new NotFoundException("L'offre specifié n'existe pas");  
        
        if(offer.getPrice() > foundCustomer.creditBalance(CDate.getDate().parse(_purchase.getDate()), conn)) throw new InvalidAmountException("Votre crédit est insuffisant pour effectuer cette opération");
        
        mg.operateur.business_logic.offer.Customer customer = new mg.operateur.business_logic.offer.
                Customer(foundCustomer.getId(), foundCustomer.getName(), foundCustomer.getEmail(), foundCustomer.getPhone_number());

        List<Purchase> purchases = purchaseRepository.findByCustomer_id(customer.getId());

        // TODO THIS LOOP
        purchases.forEach(p -> {
            try {
                p.setOffer(offerRepository.findById(p.getOffer_id()));
            } catch (RequiredException ex) {
                Logger.getLogger(PurchaseController.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        
        Account account = new Account(customer.getId(), purchases, new ArrayList<>());
        customer.setAccount(account);
        
        
        Date date = CDate.getDate().parse(_purchase.getDate());
        
        CreditConsumption creditCons = new CreditConsumption();
        creditCons.setCreated_at(date);
        creditCons.setCustomer_id(customer.getId());
        creditCons.setCons_amount(offer.getPrice());
        System.out.println(offer.getPrice());
        creditCons.insert(conn);
         
        Date endValidity = CDate.addDay(date, offer.getValidityDay());
        if (offer.getIsOneDay()) {
         endValidity = CDate.endOfDay(date);
        }
        //System.out.println(offer.getIsOneDay());
       customer.purchase(offer, date, conn, purchaseRepository);
        

    }
    
    
    
    public Offer cumulate(Offer offer) throws Exception {
        
        HashMap<String, Amount> amountMap = new HashMap<String, Amount>();
        
        for (int i = 0; i < this.amounts.size(); i++ ) {
            if (!amountMap.containsKey(this.amounts.get(i).getApplication().getName())) {
                Amount newAmount = new Amount(this.amounts.get(i));
                newAmount.setValue(0);
                amountMap.put(this.amounts.get(i).getApplication().getName(), newAmount);
            }
        }
        for (int i = 0; i < this.amounts.size(); i++ ) {
            String key = this.amounts.get(i).getApplication().getName();
            Amount amount = amountMap.get(key);
            Amount val = amountMap.get(key).add(this.amounts.get(i));
            amountMap.put(key, val);
        }
        
        for (int i = 0; i < offer.getAmounts().size(); i++ ) {
            String key = offer.getAmounts().get(i).getApplication().getName();
            Amount amount = amountMap.get(key);
            Amount val = amountMap.get(key).add(offer.getAmounts().get(i));
            amountMap.put(key, val);
        }
        
        List<Amount> val = new ArrayList<>();
        for (Map.Entry mapElement : amountMap.entrySet()) { 
            String key = (String)mapElement.getKey();
            val.add(amountMap.get(key));
        }
        return new Offer(id, name, createdAt, price, validityDay, limitation, val, priority);
    }
    
    public void retreive(Amount amount) throws Exception {
        Amount chosen = null;
        for (int i = 0; i < amounts.size(); i++) {
            if (amounts.get(i).equals(amount)) {
                chosen = amounts.get(i);
                break;
            }
        }
        double sub = chosen.getValue() - amount.getValue();
        if (sub < 0) sub = 0;
        chosen.setValue(sub);
    }
    
    public boolean isSameOfferTypeAs(Offer offer) {
        return this.equals(offer);
    }
    
    public static int getLastId(Connection conn) throws SQLException {
       return FctGen.getInt("seq", "SELECT nextVal('mg.offerSeq') as seq", conn);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        final Offer other = (Offer) obj;
        if (this.id != other.id) return false;
        return true;
    }

    @Override
    public String toString() {
        return "Offer{" + "\t\nid=" + id + ", \t\nname=" + name + ", \t\nprice=" + price + ", \t\ncreatedAt=" + createdAt + ", \t\nvalidityDay=" + validityDay + ", \t\nlimitation=" + limitation 
                + ", \t\namounts=" + amounts + '}';
    }   
}
