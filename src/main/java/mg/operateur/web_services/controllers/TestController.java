/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mg.operateur.web_services.controllers;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import java.sql.Connection;
import java.sql.SQLException;
import mg.operateur.business_logic.mobile_credit.Customer;
import mg.operateur.conn.ConnGen;
import mg.operateur.web_services.ResponseBody;
import mg.operateur.web_services.resources.consumptions.CallJSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import javax.crypto.SecretKey;
import mg.operateur.business_logic.offer.Offer;
import mg.operateur.business_logic.offer.Purchase;
import mg.operateur.business_logic.statistics.Statistic;

/**
 *
 * @author lacha
 */
@RequestMapping("/test")
@RestController
public class TestController {

    @Autowired
    PurchaseRepository purchaseRepository;

    private static final String prefix = "/test";

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
            conn = ConnGen.getConn();
            response.getData().add(new Statistic().getDeposits(conn));
        } catch (Exception ex) {
            out(ex);
        } finally {
            try { if(conn!=null) conn.close();} catch(SQLException ex) {out(ex); }
        }
        return response;
    }
}

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
