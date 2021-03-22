/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mg.operateur.web_services.controllers;

import io.jsonwebtoken.Jwts;
import java.sql.Connection;
import java.sql.SQLException;
import mg.operateur.business_logic.mobile_credit.Customer;
import mg.operateur.business_logic.offer.Operator;
import mg.operateur.business_logic.offer.PasswordHelper;
import mg.operateur.conn.Auth;
import mg.operateur.conn.ConnGen;
import mg.operateur.gen.CDate;
import mg.operateur.web_services.AuthResponseBody;
import mg.operateur.web_services.ResponseBody;
import mg.operateur.web_services.resources.commons.offer.CustomerJSON;
import static org.springframework.data.mongodb.core.mapreduce.GroupBy.key;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author lacha
 */
@RequestMapping("/auth")
@CrossOrigin(origins = "*")
@RestController
public class AuthController {
    
    private void out(Exception ex) {
        ex.printStackTrace();
        System.out.println(ex.getMessage());
    }
        
    private void setError(ResponseBody response, Exception ex) {
        response.getStatus().setCode(500);
        response.getStatus().setMessage(ex.getMessage());
    }
    
    @RequestMapping()
    public ResponseBody index() {
        ResponseBody response = new ResponseBody();
       response.getStatus().setMessage("Signin endpoint");
        return response;
    }
    
    @PostMapping("/signin")
    public ResponseBody signin(
            @RequestBody CustomerJSON _customer
    ) { 
        AuthResponseBody response = new AuthResponseBody();
        Connection conn = null;
        try {
            conn = ConnGen.getConn();
            Customer found = new Customer().find(_customer.getPhoneNumber(), conn);
            
            if (!found.getPassword().equals(PasswordHelper.md5(_customer.getPassword())))
                throw new Exception("Mot de passe ou numero incorrect");
            
            response.setToken("Bearer token");
            response.getData().add(found);
        } catch(Exception ex) {
            setError(response, ex);
            out(ex);
        } finally {
            try {
                if(conn!=null) conn.close();
            }  catch(SQLException ex) {
                setError(response, ex);
                out(ex);
            }
        }
        return response;
    }
    
    @PostMapping("/signup")
    public ResponseBody signup(
            @RequestBody CustomerJSON _customer
    ) {
        ResponseBody response = new ResponseBody();
        Connection conn = null;
        try {
            conn = ConnGen.getConn();
            Operator orange = new mg.operateur.business_logic.offer.Operator("Orange", "+26133");
            mg.operateur.business_logic.offer.Customer customer = new mg.operateur.business_logic.offer.
                    Customer(_customer.getName(), _customer.getEmail(), _customer.getPassword(), CDate.getDate().parse(_customer.getCreatedAt()), orange.issueNewPhoneNumber());
            customer.save(conn);
            Customer createdCust = new Customer().find(customer.getPhone_number(), conn);
            int id = createdCust.getId();
            
            String jws = Jwts.builder().setHeaderParam("kid", "you").setAudience(String.valueOf(id)).setIssuer("me").setSubject("Jean").signWith(Auth.getKey()).compact();
            
            customer.setToken(jws);
            response.getData().add(customer);
        } catch(Exception ex) {
            setError(response, ex);
            out(ex);
        } finally {
            try {
                if(conn!=null) conn.close();
            }  catch(SQLException ex) {
                setError(response, ex);
                out(ex);
            }
        }
        return response;
    }
}
