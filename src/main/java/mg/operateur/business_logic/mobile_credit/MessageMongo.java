/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mg.operateur.business_logic.mobile_credit;

import java.util.Date;
import org.springframework.data.annotation.Id;

/**
 *
 * @author lacha
 */
public class MessageMongo {

    private String text;
    private int customer_source;
    private int customer_dest;
    private Date date;

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
    
    
    


    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getCustomer_source() {
        return customer_source;
    }

    public void setCustomer_source(int customer_source) {
        this.customer_source = customer_source;
    }

    public int getCustomer_dest() {
        return customer_dest;
    }

    public void setCustomer_dest(int customer_dest) {
        this.customer_dest = customer_dest;
    }
    
}
