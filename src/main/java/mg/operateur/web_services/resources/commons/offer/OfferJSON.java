/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mg.operateur.web_services.resources.commons.offer;

import java.util.Date;
import java.util.List;
import mg.operateur.business_logic.offer.Amount;

/**
 *
 * @author dodaa
 */
public class OfferJSON {
    private int id;
    private String name;
    private double price;
    private Date createdAt;
    private int validityDay;
    private LimitationJSON limitation;
    private int priority;
    private List<AmountJSON> amounts;

    public OfferJSON() {
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public void setId(int id) {
        this.id = id;
    }
    public void setName(String name) throws Exception { 
        this.name = name; 
    }
    public void setCreatedAt(Date createdAt) throws Exception {
        this.createdAt = createdAt; 
    }
    public void setPrice(double price) throws Exception {
        this.price = price; 
    }
    public void setValidityDay(int validityDay) throws Exception { 
        this.validityDay = validityDay; 
    }
    public void setLimitation(LimitationJSON limitation) throws Exception { 
        this.limitation = limitation;
    }
    public void setAmounts(List<AmountJSON> amounts) throws Exception {
        this.amounts = amounts;
    }
    
    public int getId() { return id; }
    public String getName() { return name; }
    public Date getCreatedAt() { return createdAt; }
    public double getPrice() { return price; }
    public int getValidityDay() { return validityDay; }
    public LimitationJSON getLimitation() { return limitation; }
    public List<AmountJSON> getAmounts() { return amounts; }
}
