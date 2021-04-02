/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mg.operateur.business_logic.notifications;

import java.sql.Connection;
import java.text.ParseException;
import java.util.Date;
import java.util.List;
import mg.operateur.business_logic.mobile_credit.Customer;
import mg.operateur.conn.ConnGen;
import mg.operateur.gen.CDate;
import mg.operateur.gen.FctGen;

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
    
    public void insert(Connection conn) throws Exception {
        FctGen.insert(this, columns(), "mg.notifs", conn);
    }
    
    public List<Object> findByPhoneNumber(String phoneNumber) throws Exception {
        Connection conn = ConnGen.getConn();
        Customer customer = new Customer().findTrue(phoneNumber);
        return FctGen.findAll(new NotifyMessage(), "SELECT * from mg.notifs WHERE customer_id = " + customer.getId(), columns(), conn);
    }
    
    public String[] columns() {
        return new String[] {"message", "customer_id", "date" };
    }
    
}
