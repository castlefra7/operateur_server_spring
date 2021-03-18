/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mg.operateur.web_services.controllers;

import java.sql.Connection;
import java.sql.SQLException;
import mg.operateur.business_logic.offer.Operator;
import mg.operateur.conn.ConnGen;
import mg.operateur.web_services.ResponseBody;
import mg.operateur.web_services.resources.commons.offer.CustomerJSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author lacha
 */
@CrossOrigin(origins = "*")
@RestController
public class SignupController {
    
    private static final String PREFIX = "signup";
    
    @Autowired
    private OfferRepository offerRepository;
    
    private void out(Exception ex) {
        ex.printStackTrace();
        System.out.println(ex.getMessage());
    }
        
    private void setError(ResponseBody response, Exception ex) {
        response.getStatus().setCode(500);
        response.getStatus().setMessage(ex.getMessage());
    }
    
    @RequestMapping(PREFIX + "/ping")
    public ResponseBody index() {
        ResponseBody response = new ResponseBody();
       response.getStatus().setMessage("Signup endpoint");
        return response;
    }
    
    @PostMapping(PREFIX)
    public ResponseBody signup(
            @RequestBody CustomerJSON _customer
    ) {
        ResponseBody response = new ResponseBody();
        Connection conn = null;
        try {
            conn = ConnGen.getConn();
            Operator orange = new mg.operateur.business_logic.offer.Operator("Orange", "+26133");
            mg.operateur.business_logic.offer.Customer customer = new mg.operateur.business_logic.offer.
                    Customer(_customer.getName(), _customer.getEmail(), _customer.getPassword(), _customer.getCreatedAt(), orange.issueNewPhoneNumber());
            customer.save(conn);
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
