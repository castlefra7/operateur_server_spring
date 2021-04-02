/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mg.operateur.business_logic.notifications;

import java.text.ParseException;
import java.util.Date;
import mg.operateur.gen.CDate;

/**
 *
 * @author dodaa
 */
public class NotifyMessage {
    private String message;
    private int customer_id;
    private Date date;

    public NotifyMessage() {
    }

    public NotifyMessage(String message, int customer_id, String date) throws ParseException {
        this.message = message;
        this.customer_id = customer_id;
        this.date = CDate.getDate().parse(date);
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getCustomer_id() {
        return customer_id;
    }

    public void setCustomer_id(int customer_id) {
        this.customer_id = customer_id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
    
}
