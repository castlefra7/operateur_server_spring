package mg.operateur.web_services.controllers;

import java.sql.Connection;
import java.sql.SQLException;
import mg.operateur.business_logic.mobile_credit.CallPricing;
import mg.operateur.business_logic.mobile_credit.InternetPricing;
import mg.operateur.business_logic.mobile_credit.MessagePricing;
import mg.operateur.business_logic.pricings.Pricing;
import mg.operateur.conn.ConnGen;
import mg.operateur.gen.CDate;
import mg.operateur.web_services.ResponseBody;
import mg.operateur.web_services.resources.pricings.InternetPricingJSON;
import mg.operateur.web_services.resources.pricings.MessagePricingJSON;
import mg.operateur.web_services.resources.pricings.PricingJSON;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
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
@RequestMapping("/pricings")
@CrossOrigin(origins = "*")
@RestController
public class PricingController {
    
    private void out(Exception ex) {
        ex.printStackTrace();
        System.out.println(ex.getMessage());
    }
        
    private void setError(ResponseBody response, Exception ex) {
        response.getStatus().setCode(500);
        response.getStatus().setMessage(ex.getMessage());
    }
    
    @GetMapping()
    public ResponseBody index() {
        ResponseBody response = new ResponseBody();
        response.getStatus().setMessage("Tarifs EndPoint");
        return response;
    }
    
    @PostMapping("/messages")
    public ResponseBody messageCallPricing(@RequestBody MessagePricingJSON _message) {
        ResponseBody response = new ResponseBody();
        Connection conn = null;
        try {
            conn = ConnGen.getConn();
            MessagePricing pricing = new MessagePricing();
            pricing.setCreated_at(CDate.getDate().parse(_message.getDate()));
            pricing.setAmount_exterior(_message.getAmount_exterior());
            pricing.setAmount_interior(_message.getAmount_interior());
            pricing.setUnit(_message.getUnit());
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
    
    @PostMapping("/calls")
    public ResponseBody messagePricing(@RequestBody PricingJSON _message) {
        ResponseBody response = new ResponseBody();
        Connection conn = null;
        try {
            conn = ConnGen.getConn();
            CallPricing pricing = new CallPricing();
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
    
    
    @PostMapping("/internet")
    public ResponseBody internetPricing(@RequestBody InternetPricingJSON _internet) {
        ResponseBody response = new ResponseBody();
        Connection conn = null;
        try {
            conn = ConnGen.getConn();
            InternetPricing pricing = new InternetPricing();
            pricing.setCreated_at(CDate.getDate().parse(_internet.getDate()));
            pricing.setAmount(_internet.getAmount());
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
