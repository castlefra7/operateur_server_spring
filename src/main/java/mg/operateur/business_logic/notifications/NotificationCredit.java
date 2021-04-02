/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mg.operateur.business_logic.notifications;

import java.sql.Connection;
import java.util.logging.Level;
import java.util.logging.Logger;
import mg.operateur.business_logic.mobile_credit.Customer;
import mg.operateur.conn.ConnGen;
import mg.operateur.gen.CDate;

/**
 *
 * @author lacha
 */
public class NotificationCredit {
    
    public static NotifyMessage notifyCredit(double amount, String date, String phoneNumber) {
        try {
            Connection conn = ConnGen.getConn();
            Customer found = new Customer().find(phoneNumber, conn);
            String message = "Votre achat de " + amount + " Ar de crédit le "+ CDate.format(CDate.getDate().parse(date)) +" sur votre compte a été effectué avec succès.";
            NotifyMessage notifyMessage = new NotifyMessage(message, found.getId(), date);
            notifyMessage.insert(conn);
            return notifyMessage;
        } catch (Exception ex) {
            Logger.getLogger(NotificationCredit.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
        public static NotifyMessage notifyTransfertCredit(double amount, String date, String phoneNumberSrc, String phoneNumberDest) {
        try {
            Connection conn = ConnGen.getConn();
            Customer found = new Customer().find(phoneNumberSrc, conn);
            String message = "Le transfert de " + amount + " Ar de crédit vers le numero " + phoneNumberDest + " le "+ CDate.format(CDate.getDate().parse(date)) +" a été effectué avec succès.";
            NotifyMessage notifyMessage = new NotifyMessage(message, found.getId(), date);
            System.out.println(notifyMessage);
            notifyMessage.insert(conn);
            return notifyMessage;
        } catch (Exception ex) {
            Logger.getLogger(NotificationCredit.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
}
