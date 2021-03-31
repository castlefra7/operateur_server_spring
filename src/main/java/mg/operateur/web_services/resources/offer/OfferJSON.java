/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mg.operateur.web_services.resources.offer;

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
    private String createdAt;
    private int validityDay;
    private LimitationJSON limitation;
    private int priority;
    private List<AmountJSON> amounts;
    private String code;
    
    private int hourMin;
    private int hourMax;

    public int getHourMin() {
        return hourMin;
    }

    public void setHourMin(int hourMin) {
        this.hourMin = hourMin;
    }

    public int getHourMax() {
        return hourMax;
    }

    public void setHourMax(int hourMax) {
        this.hourMax = hourMax;
    }
    
    

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
    
    
    
    private boolean isOneDay;

    public boolean getIsOneDay() {
        return isOneDay;
    }

    public void setIsOneDay(boolean isOneDay) {
        this.isOneDay = isOneDay;
    }

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
    public void setCreatedAt(String createdAt) throws Exception {
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
    public String getCreatedAt() { return createdAt; }
    public double getPrice() { return price; }
    public int getValidityDay() { return validityDay; }
    public LimitationJSON getLimitation() { return limitation; }
    public List<AmountJSON> getAmounts() { return amounts; }
}
