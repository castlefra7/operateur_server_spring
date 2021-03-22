/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mg.operateur.business_logic.statistics;

/**
 *
 * @author lacha
 */
public class OfferStat {
    private int id;
    private String name;
    private int count;
    
    public OfferStat() {
        
    }
    
       
    public OfferStat(int _id, String _name, int _count) {
        setId(_id);
        setName(_name);
        setCount(_count);
    }

    
    public OfferStat(int _id, String _name) {
        setId(_id);
        setName(_name);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    
    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }    
    
    @Override
    public boolean equals(Object ob) {
        OfferStat offer =  (OfferStat)ob;
        return this.getId() == offer.getId() && offer.getName().equals(this.getName());
    }

}
