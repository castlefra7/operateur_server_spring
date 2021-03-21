/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mg.operateur.web_services.controllers;

import java.sql.Connection;
import java.sql.SQLException;
import mg.operateur.business_logic.mobile_credit.Customer;
import mg.operateur.business_logic.offer.PasswordHelper;
import mg.operateur.conn.ConnGen;
import mg.operateur.web_services.AuthResponseBody;
import mg.operateur.web_services.ResponseBody;
import mg.operateur.web_services.resources.commons.offer.CustomerJSON;
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
public class AuthController {
    
    private static final String PREFIX = "auth";
    
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
       response.getStatus().setMessage("Signin endpoint");
        return response;
    }
    
    @PostMapping(PREFIX)
    public ResponseBody signup(
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
}
