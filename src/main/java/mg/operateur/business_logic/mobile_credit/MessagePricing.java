/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mg.operateur.business_logic.mobile_credit;

/**
 *
 * @author lacha
 */
public class MessagePricing extends Pricing {
    
    private int unit;

    public int getUnit() {
        return unit;
    }

    public void setUnit(int unit) {
        this.unit = unit;
    }
    
    

    @Override
    public String tableName() {
        return "mg.messages_pricings";
    }
    
    @Override
    public String[] columns() {
        return new String[]{"amount_interior", "amount_exterior", "created_at", "unit"};
    }

  
}
