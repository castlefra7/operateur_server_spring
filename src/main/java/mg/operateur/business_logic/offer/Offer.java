/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mg.operateur.business_logic.offer;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.data.annotation.Id;

/**
 *
 * @author dodaa
 */
public class Offer {
    
    @Id
    private int id;
    private String name;
    private double price;
    private Date createdAt;
    private int validityDay;
    private Limitation limitation;
    private List<Amount> amounts;

    public Offer() {
    }

    public Offer(int id, String name, Date createdAt, double price, int validityDay, Limitation limitation, List<Amount> amounts) throws Exception {
        setId(id);
        setName(name);
        setCreatedAt(createdAt);
        setPrice(price);
        setValidityDay(validityDay);
        setLimitation(limitation);
        setAmounts(amounts);
    }
    
    public Offer(String name, Date createdAt, double price, int validityDay, Limitation limitation, List<Amount> amounts) throws Exception {
        setName(name);
        setCreatedAt(createdAt);
        setPrice(price);
        setValidityDay(validityDay);
        setLimitation(limitation);
        setAmounts(amounts);
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
    
    public int getId() { return id; }
    public String getName() { return name; }
    public Date getCreatedAt() { return createdAt; }
    public double getPrice() { return price; }
    public int getValidityDay() { return validityDay; }
    public Limitation getLimitation() { return limitation; }
    public List<Amount> getAmounts() { return amounts; }
    
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
        return new Offer(id, name, createdAt, price, validityDay, limitation, val);
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
