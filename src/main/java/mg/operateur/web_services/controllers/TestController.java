/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mg.operateur.web_services.controllers;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import mg.operateur.web_services.ResponseBody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import mg.operateur.business_logic.mobile_credit.MessageMongo;
import mg.operateur.business_logic.offer.Purchase;
import mg.operateur.gen.CDate;

/**
 *
 * @author lacha
 */
@RequestMapping("/test")
@RestController
public class TestController {

    @Autowired
    PurchaseRepository purchaseRepository;
    @Autowired
    MessageRepository messageRepo;

    private void out(Exception ex) {
        ex.printStackTrace();
        System.out.println(ex.getMessage());
    }

    private void setError(ResponseBody response, Exception ex) {
        response.getStatus().setCode(500);
        response.getStatus().setMessage(ex.getMessage());
    }

    @GetMapping("/error")
    public ResponseBody error() {
        ResponseBody response = new ResponseBody();
        response.getStatus().setCode(404);
        response.getStatus().setMessage("Ressource introuvable");
        return response;
    }

    @RequestMapping()
    public ResponseBody index() {
        ResponseBody response = new ResponseBody();
        Connection conn = null;
        try {
            List<Purchase> sorted = new ArrayList();
            Purchase p1 = new Purchase();
            p1.setEndDate(CDate.getDate().parse("2021-04-01 08:00"));
            Purchase p2 = new Purchase();
            p2.setEndDate(CDate.getDate().parse("2021-04-01 09:00"));
            
            Purchase p3 = new Purchase();
            p3.setEndDate(CDate.getDate().parse("2021-04-01 10:00"));
            
            sorted.add(p1);
            sorted.add(p3);
            sorted.add(p2);
         
            Collections.sort(sorted);
            
            for(Purchase p: sorted) {
                System.out.println(p.getEndDate());
            }

        } catch (Exception ex) {
            out(ex);
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException ex) {
                out(ex);
            }
        }
        return response;
    }

}


/*Date date = new Date();
            Calendar c = new GregorianCalendar();
            c.setTime(date);
            c.set(Calendar.YEAR, 2022);
            c.set(Calendar.MONTH, 1);
            c.set(Calendar.DAY_OF_MONTH, 3);
            c.set(Calendar.HOUR, 20);
            response.getData().add(purchaseRepository.findByCustomer_id(4));*/
 /*String s = "2021-03-28T16:21:53.836Z";
            TemporalAccessor ta = DateTimeFormatter.ISO_OFFSET_DATE_TIME.parse(s);
            Instant i = Instant.from(ta);
            Date d = Date.from(i);
            response.getData().add(d.toString());
 */
 /*
System.setProperty("user.timezone", "UTC");

Date nowUtc = new Date();
            TimeZone tana = TimeZone.getTimeZone("UTC");

            Calendar nowTana = Calendar.getInstance(tana);
            nowTana.setTime(nowUtc);
 */

 /*Connection conn = null;
        String secretString = "123123123112312312311231231231222";
        SecretKey key;
        key = Keys.hmacShaKeyFor(secretString.getBytes(StandardCharsets.UTF_8));
        
        System.out.println("/off".split("/")[1]);
        
        try {
            String jws = Jwts.builder().setHeaderParam("kid", "you").setAudience("you").setIssuer("me").setSubject("Jean").signWith(key).compact();
            response.getData().add(jws);
        } catch (Exception ex) {
            this.setError(response, ex);
            out(ex);
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException ex) {
                out(ex);
            }
        }

        Jws<Claims> jws;
        try {
            jws = Jwts.parserBuilder() // (1)
                    .setSigningKey(key) // (2)
                    .build() // (3)
                    .parseClaimsJws("eyJraWQiOiJ5b3UiLCJhbGciOiJIUzI1NiJ9.eyJhdWQiOiJ5b3UiLCJpc3MiOiJtZSIsInN1YiI6IkplYW4ifQ.kW7_ApJXKEiEhvGmf7DiclRKgbn3gL6EYU5hzcKiT70"); // (4)

            System.out.println(jws.getBody().getSubject());
            System.out.println(jws.getBody().getAudience());
            System.out.println(jws.getBody().getIssuer());

            // we can safely trust the JWT
        } catch (JwtException ex) {       // (5)
            this.setError(response, ex);
            out(ex);
        }*/
 /*

    List<Purchase> all = new ArrayList();
    Offer o1 = new Offer();
    o1.setPriority(15);
    Offer o2 = new Offer();
    o2.setPriority(3);

    all.add(new Purchase(1, 1, o1, new Date(), 1));
    all.add(new Purchase(2, 1, o2, new Date(), 1));

    Collections.sort(all);

    for (Purchase p : all) {
        System.out.println(p.getId());
    }

 */
