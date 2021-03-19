/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mg.operateur.test.offer;

import mg.operateur.business_logic.offer.Operator;
import mg.operateur.business_logic.offer.SmartDate;
import mg.operateur.business_logic.offer.Offer;
import mg.operateur.business_logic.offer.Limitation;
import mg.operateur.business_logic.offer.Application;
import mg.operateur.business_logic.offer.Unit;
import mg.operateur.business_logic.offer.Customer;
import mg.operateur.business_logic.offer.Amount;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 *
 * @author dodaa
 */
public class Test {
    public static void main(String[] args) {
        try {
            
            Limitation faceboobakaLimit = new Limitation(2, 7);
            Limitation yellow100Limit = new Limitation(10, 1);
            
            Amount amount = new Amount(new Application(1, "Facebook", new Unit(1, "Mo")), 1000);
            List<Amount> faceboobakaAmounts = new ArrayList<Amount>();
            faceboobakaAmounts.add(amount);
            
            Amount internet = new Amount(new Application(2, "Internet", new Unit(1, "Mo")), 20);
            Amount messages = new Amount(new Application(2, "Message", new Unit(2, "SMS")), 20);
            Amount appels = new Amount(new Application(2, "Appel", new Unit(1, "Min")), 20);
            
            List<Amount> yellow100Amounts = new ArrayList<Amount>();
            yellow100Amounts.add(internet);
            yellow100Amounts.add(messages);
            yellow100Amounts.add(appels);
            
            Offer faceboobaka = new Offer(1, "faceboobaka", SmartDate.now(), 1000, 7, faceboobakaLimit, faceboobakaAmounts, 1);
            Offer yellow100 = new Offer(2, "yellow100", SmartDate.now(), 100, 1, yellow100Limit, yellow100Amounts, 2);
            
            Operator telma = new Operator(1, "telma", "034");
            
            Customer baks = new Customer(1, "Baks", "andri.bakoson@gmail.com", telma.issueNewPhoneNumber());

//            baks.purchase(faceboobaka, SmartDate.now());
//            baks.purchase(faceboobaka, SmartDate.now());
//            baks.purchase(yellow100, SmartDate.now());
//            baks.purchase(yellow100, SmartDate.now());
//            baks.purchase(yellow100, SmartDate.now());
//            baks.purchase(yellow100, SmartDate.now());
//            baks.purchase(yellow100, SmartDate.now());
//            baks.purchase(yellow100, SmartDate.now());
//            baks.purchase(yellow100, SmartDate.now());
//            baks.purchase(yellow100, SmartDate.now());
//            baks.purchase(yellow100, SmartDate.now());
//            baks.purchase(yellow100, SmartDate.now());

            List<Offer> infoConsoAt = baks.getAccount().getPurchasesTotalAt(SmartDate.now().addDays(1));
            System.out.println(infoConsoAt);
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
