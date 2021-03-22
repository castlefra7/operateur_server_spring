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
import mg.operateur.gen.CDate;
import mg.operateur.web_services.ResponseBody;
import mg.operateur.web_services.resources.commons.offer.CustomerJSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author lacha
 */
@RequestMapping("/signup")
@CrossOrigin(origins = "*")
@RestController
public class SignupController {
    
    
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
    
    @RequestMapping("/ping")
    public ResponseBody index() {
        ResponseBody response = new ResponseBody();
       response.getStatus().setMessage("Signup endpoint");
        return response;
    }

}
