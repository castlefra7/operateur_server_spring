package mg.operateur.web_services.controllers;

import java.sql.Connection;
import java.sql.SQLException;
import mg.operateur.business_logic.pricings.Pricing;
import mg.operateur.conn.ConnGen;
import mg.operateur.gen.CDate;
import mg.operateur.web_services.ResponseBody;
import mg.operateur.web_services.resources.pricings.PricingJSON;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author lacha
 */
@RestController
public class PricingController {
    private static final String prefix = "/pricings";
    
    private void out(Exception ex) {
        ex.printStackTrace();
        System.out.println(ex.getMessage());
    }
        
    private void setError(ResponseBody response, Exception ex) {
        response.getStatus().setCode(500);
        response.getStatus().setMessage(ex.getMessage());
    }
    
    @GetMapping(prefix)
    public ResponseBody index() {
        ResponseBody response = new ResponseBody();
        response.getStatus().setMessage("Tarifs EndPoint");
        return response;
    }
    
    @PostMapping(prefix)
    public ResponseBody messageCallPricing(@RequestBody PricingJSON _message) {
        ResponseBody response = new ResponseBody();
        Connection conn = null;
        try {
            conn = ConnGen.getConn();
            Pricing pricing = new Pricing();
            pricing.setApplication_id(_message.getApplication_id());
            pricing.setCreated_at(CDate.getDate().parse(_message.getDate()));
            pricing.setAmount_exterior(_message.getAmount_exterior());
            pricing.setAmount_interior(_message.getAmount_interior());
            pricing.insert(conn);
            response.getStatus().setMessage("Succés");
        } catch(Exception ex) {
            this.setError(response, ex);
            out(ex);
        } finally {
            try {
                if(conn!=null) conn.close();
            } catch(SQLException ex) {
                out(ex);
            }
        }
        return response;
    }
    
    
    @PostMapping(prefix + "/internet")
    public ResponseBody internetPricing(@RequestBody PricingJSON _message) {
        ResponseBody response = new ResponseBody();
        Connection conn = null;
        try {
            conn = ConnGen.getConn();
            Pricing pricing = new Pricing();
            pricing.setApplication_id(_message.getApplication_id());
            pricing.setCreated_at(CDate.getDate().parse(_message.getDate()));
            pricing.setAmount_exterior(_message.getAmount_interior());
            pricing.setAmount_interior(_message.getAmount_interior());
            pricing.insert(conn);
            response.getStatus().setMessage("Succés");
        } catch(Exception ex) {
            this.setError(response, ex);
            out(ex);
        } finally {
            try {
                if(conn!=null) conn.close();
            } catch(SQLException ex) {
                out(ex);
            }
        }
        return response;
    }
    
}
