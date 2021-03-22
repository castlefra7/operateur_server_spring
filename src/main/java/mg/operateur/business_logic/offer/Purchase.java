/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mg.operateur.business_logic.offer;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import mg.operateur.gen.FctGen;
import mg.operateur.gen.RequiredException;
import mg.operateur.web_services.controllers.PurchaseRepository;
import org.springframework.data.annotation.Id;

/**
 *
 * @author dodaa
 */
public final class Purchase implements Comparable {
    
    @Id
    private int id;
    private int customer_id;
    private Customer customer;
    private Date date;
    private Offer offer;
    private int offer_id;
    
    public Purchase() {}

    public Purchase(int id, int customerId, Offer purchasedOffer, Date purchaseDate, int offer_id) throws RequiredException {
        setId(id);
        setCustomer_id(customerId);
        setOffer(purchasedOffer);
        setDate(purchaseDate);
        setOffer_id(offer_id);
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public int getOffer_id() {
        return offer_id;
    }

    public void setOffer_id(int offer_id) {
        this.offer_id = offer_id;
    }
    
    public void setDate(Date date) throws RequiredException {
        if (date == null)
            throw new RequiredException("purchase date is required");
        this.date = date;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public void setCustomer_id(int customer_id) {
        this.customer_id = customer_id;
    }
    
    public void setOffer(Offer offer) throws RequiredException {
        if (offer == null)
            throw new RequiredException("purchased offer is required");
        this.offer = offer;
    }

    public int getId() { return id; }
    public int getCustomer_id() { return customer_id; }
    public Offer getOffer() { return offer; }
    public Date getDate() { return date; }

    public boolean isStillValidAt(Date newPurchaseDate) {
        int validityDay = offer.getValidityDay();
        SmartDate smartDate = new SmartDate(date.getTime());
        SmartDate addDays = smartDate.addDays(validityDay);
        return newPurchaseDate.before(addDays);
    }
    
    public static int getNextId(Connection conn) throws SQLException {
        return FctGen.getInt("seq", "SELECT nextval('mg.purhaseSeq') as seq", conn);
    }
    
//    public void save(Connection conn) 
//            throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, SQLException {
//        FctGen.insert(this, new String[] {"customer_id", "date", "offer_id"} , "mg.offer_purchases", conn);
//    }
    
//    public static List<Purchase> findByCustomerId(int customerId, Connection conn) throws SQLException, Exception {
//        PreparedStatement pst = null;
//        List<Purchase> list = new ArrayList<>();
//        try{
//            pst = conn.prepareStatement("SELECT * FROM mg.offer_purchases WHERE customer_id = ?");
//            pst.setInt(1, customerId);
//            ResultSet res = pst.executeQuery();
//            
//            while (res.next()) {
//                
//                list.add(new Purchase(
//                        res.getInt("customer_id"),
//                        new Offer(),
//                        res.getDate("date"),
//                        res.getInt("offer_id")
//                    )
//                );
//            }
//            return list;
//        }catch(Exception ex){   
//            throw ex;
//        }finally{
//            if(pst!=null)pst.close();
//        }
//    }
    
    public static List<Purchase> findByCustomerId(int customerId, PurchaseRepository repo) {
        return repo.findByCustomer_id(customerId);
    }

    @Override
    public String toString() {
        return "OfferPurchase{" + "\t\nid=" + id + ", \t\ncustomerId=" + customer_id + ", \t\npurchaseDate=" + date + ", \t\noffer=" + offer + '}';
    }

    @Override
    public int compareTo(Object o) {
        Purchase purchase = (Purchase)o;
        if(this.getOffer().getPriority() > purchase.getOffer().getPriority()) {
            return 1;
        } else if (this.getOffer().getPriority() < purchase.getOffer().getPriority()) {
            return -1;
        } else {
            return 0;
        }
    }
}
