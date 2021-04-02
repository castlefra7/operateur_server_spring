/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mg.operateur.business_logic.notifications;

import java.sql.Connection;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import mg.operateur.business_logic.mobile_credit.Customer;
import mg.operateur.conn.ConnGen;
import mg.operateur.gen.CDate;

/**
 *
 * @author lacha
 */
public class NotificationMobileMoney {
    
    public static NotifyMessage notifyDeposit(double amount, String date, String phoneNumber) {
        try {
            Connection conn = ConnGen.getConn();
            Customer found = new Customer().find(phoneNumber, conn);
            String message = "Le dépôt de " + amount + " Ar sur votre compte le "+ CDate.format(CDate.getDate().parse(date)) +" a été effectué avec succès.\n En attente de validation.";
            NotifyMessage notifyMessage = new NotifyMessage(message, found.getId(), date);
            notifyMessage.insert(conn);
            return notifyMessage;
        } catch (Exception ex) {
            Logger.getLogger(NotificationMobileMoney.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    public static NotifyMessage notifyWithdraw(double amount, String date, String phoneNumber) {
        try {
            Connection conn = ConnGen.getConn();
            Customer found = new Customer().find(phoneNumber, conn);
            String message = "Le retrait de " + amount + " Ar depuis votre compte le " + CDate.format(CDate.getDate().parse(date)) + " a été effectué avec succès.\n";
            NotifyMessage notifyMessage = new NotifyMessage(message, found.getId(), date);
            notifyMessage.insert(conn);
            return notifyMessage;
        } catch (Exception ex) {
            Logger.getLogger(NotificationMobileMoney.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    public static NotifyMessage notifyTransfert(double amount, String date, String phoneNumberSrc, String phoneNumberDest) {
        try {
            Connection conn = ConnGen.getConn();
            Customer found = new Customer().find(phoneNumberSrc, conn);
            String message = "Le transfert de " + amount + " Ar depuis votre compte vers le numéro " + phoneNumberDest + " le " + CDate.format(CDate.getDate().parse(date)) + " a été effectué avec succès.\n";
            NotifyMessage notifyMessage = new NotifyMessage(message, found.getId(), date);
            notifyMessage.insert(conn);
            return notifyMessage;
        } catch (Exception ex) {
            Logger.getLogger(NotificationMobileMoney.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    public static NotifyMessage notiBuyCredit(double amount, String date, String phoneNumber) {
        try {
            Connection conn = ConnGen.getConn();
            Customer found = new Customer().find(phoneNumber, conn);
            String message = "Votre achat de crédit de " + amount + " Ar depuis votre compte Mobile Money le " + CDate.format(CDate.getDate().parse(date)) + " a été effectué avec succès.\n";
            NotifyMessage notifyMessage = new NotifyMessage(message, found.getId(), date);
            notifyMessage.insert(conn);
            return notifyMessage;
        } catch (Exception ex) {
            Logger.getLogger(NotificationMobileMoney.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
}
