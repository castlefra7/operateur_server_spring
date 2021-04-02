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
public class NotificationOffer {
    
    public static NotifyMessage notifyPurchase(String offerName, String date, String phoneNumber) {
        try {
            Connection conn = ConnGen.getConn();
            Customer found = new Customer().find(phoneNumber, conn);
            String message = "L'achat du forfait " + offerName + " le "+ CDate.format(CDate.getDate().parse(date)) +" sur votre compte a été effectué avec succès.";
            return new NotifyMessage(message, found.getId(), date);
        } catch (Exception ex) {
            Logger.getLogger(NotificationOffer.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    public static NotifyMessage notifyPurchaseFromMobileMoney(String offerName, String date, String phoneNumber) {
        try {
            Connection conn = ConnGen.getConn();
            Customer found = new Customer().find(phoneNumber, conn);
            String message = "L'achat du forfait " + offerName + " le "+ CDate.format(CDate.getDate().parse(date)) +" depuis votre compte Mobile Money a été effectué avec succès.";
            return new NotifyMessage(message, found.getId(), date);
        } catch (Exception ex) {
            Logger.getLogger(NotificationOffer.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
}
