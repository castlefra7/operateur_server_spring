/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mg.operateur.business_logic.statistics;

import java.lang.reflect.InvocationTargetException;
import java.net.URISyntaxException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Iterator;
import mg.operateur.business_logic.offer.Purchase;
import mg.operateur.conn.ConnGen;
import mg.operateur.gen.FctGen;
import mg.operateur.web_services.controllers.PurchaseRepository;

/**
 *
 * @author lacha
 */
public class Statistic {
    
    public Chart getDeposits(Connection conn) throws SQLException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException {
        List<Object> ob = FctGen.findAll(new AmountStat(), "select * from mg.deposits_stat", new String[]  {"date", "amount" }, conn);
        List<String> labels = new ArrayList();
        List<Double> data = new ArrayList();
        for(Object o: ob) {
            AmountStat amount = (AmountStat)o;
            labels.add(amount.getDate());
            data.add(amount.getAmount());
        }
        Chart ch =new Chart();
        ch.setLabels(labels);
        ch.setData(data);
        return ch;
    }
    
    public Chart getWithdraws(Connection conn) throws SQLException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException {
        List<Object> ob = FctGen.findAll(new AmountStat(), "select * from mg.withdraws_stat", new String[]  {"date", "amount" }, conn);
        List<String> labels = new ArrayList();
        List<Double> data = new ArrayList();
        for(Object o: ob) {
            AmountStat amount = (AmountStat)o;
            labels.add(amount.getDate());
            data.add(amount.getAmount());
        }
        Chart ch =new Chart();
        ch.setLabels(labels);
        ch.setData(data);
        return ch;
    }
    
    public List<OfferStat> getOffersStat(Date date, PurchaseRepository purchase) throws SQLException, URISyntaxException {
        Connection conn = null;
        try {
            conn = ConnGen.getConn();
            return getOffersStat(date, purchase, conn);
        } catch(SQLException ex ) {
            throw ex;
        } finally {
            if(conn!=null) conn.close();
        }
    }
    
    public List<OfferStat> getOffersStat(Date date, PurchaseRepository purchase, Connection conn) {
        List<OfferStat> result = new ArrayList();
        List<Purchase> all = purchase.findAll();
        HashMap<Integer, Integer> counts = new HashMap<>();
        HashSet<OfferStat> offersName = new HashSet();
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR, 23);
        calendar.set(Calendar.MINUTE, 59);
        date = calendar.getTime();
        for(Purchase p: all) {
            if(p.getDate().compareTo(date) <= 0 ){
                Integer newCount = counts.get(p.getOffer().getId());
                if(newCount != null) {
                    newCount ++;
                } else {
                    newCount = 1;
                }
                counts.put(p.getOffer().getId(), newCount);
                offersName.add(new OfferStat(p.getOffer().getId(), p.getOffer().getName()));
            }
        }
        
        
        Iterator iterator = counts.entrySet().iterator();
        while(iterator.hasNext()) {
            Map.Entry<Integer, Integer> me = (Map.Entry)iterator.next();
            Iterator offersNameIt = offersName.iterator();
            String name = "";
            while(offersNameIt.hasNext()) {
                OfferStat off = (OfferStat)offersNameIt.next();
                if(me.getKey() == off.getId()) {
                    name = off.getName();
                    break;
                }
            }
            result.add(new OfferStat(me.getKey(), name, me.getValue()));
        }
        
        return result;
    }
    
}
