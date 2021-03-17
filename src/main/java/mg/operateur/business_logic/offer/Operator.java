/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mg.operateur.business_logic.offer;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

/**
 *
 * @author dodaa
 */
public class Operator {
    
    private int id;
    private String name;
    private String phoneNumberPrefix;
    private List<Offer> offers;

    public Operator() {
    }
    
    public Operator(int id, String name, String phoneNumberPrefix) throws Exception {
        setId(id);
        setName(name);
        setPhoneNumberPrefix(phoneNumberPrefix);
        offers = new ArrayList<>();
    }
    
    public Operator(String name, String phoneNumberPrefix) throws Exception {
        setName(name);
        setPhoneNumberPrefix(phoneNumberPrefix);
        offers = new ArrayList<>();
    }
    
    public void setOffers(List<Offer> offers) {
        this.offers = offers;
    }
    
    public List<Offer> getOffers() {
        return offers;
    }
    
    public String getName() {
        return name;
    }

    public void setName(String name) throws Exception {
        if (name == null)
            throw new Exception("Operator name must not be null");
        this.name = name;
    }

    public String getPhoneNumberPrefix() {
        return phoneNumberPrefix;
    }

    public int getId() { return id; }

    public void setId(int id) {
        this.id = id;
    }

    public void setPhoneNumberPrefix(String phoneNumberPrefix) throws Exception {
        if (phoneNumberPrefix == null)
            throw new Exception("PhonNumPrefException : Prefix must not be null");
        if (phoneNumberPrefix.length() != 3)
            throw new Exception("PhonNumPrefException : Prefix must be a string of length 3");
        this.phoneNumberPrefix = phoneNumberPrefix;
    }
    
    public PhoneNumber issueNewPhoneNumber() {
        String generatedNumber = generatePhoneNumber();
        PhoneNumber newPhoneNumber = new PhoneNumber(this);
        newPhoneNumber.setValue(phoneNumberPrefix + " " + generatedNumber);
        return newPhoneNumber;
    }

    private String generatePhoneNumber() {
        int num1, num2, num3;
        Random generator = new Random();
        num1 = generator.nextInt(20) + 1;
        num2 = generator.nextInt(999);
        num3 = generator.nextInt(80);
        return String.format("%02d", num1) + " " + String.format("%03d", num2) + " " + String.format("%02d", num3);
    }

    public void createOffer(Offer offer) {
        offers.add(offer);
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Operator other = (Operator) obj;
        if (!Objects.equals(this.name, other.name)) {
            return false;
        }
        if (!Objects.equals(this.phoneNumberPrefix, other.phoneNumberPrefix)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Operator{" + "\t\nname=" + name + ", \t\nphoneNumberPrefix=" + phoneNumberPrefix;
    }
}
