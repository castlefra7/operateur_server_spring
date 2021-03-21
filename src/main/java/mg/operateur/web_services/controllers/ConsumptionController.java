/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mg.operateur.web_services.controllers;

import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.ParseException;
import mg.operateur.business_logic.mobile_credit.Customer;
import mg.operateur.conn.ConnGen;
import mg.operateur.gen.InvalidAmountException;
import mg.operateur.gen.InvalidDateException;
import mg.operateur.gen.InvalidFormatException;
import mg.operateur.gen.NotFoundException;
import mg.operateur.web_services.ResponseBody;
import mg.operateur.web_services.resources.consumptions.CallJSON;
import mg.operateur.web_services.resources.consumptions.InternetJSON;
import mg.operateur.web_services.resources.consumptions.MessageJSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author lacha
 */
@RequestMapping("/consume")
@CrossOrigin(origins = "*")
@RestController
public class ConsumptionController {
    @Autowired
    PurchaseRepository purchaseRepository;
    
    
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
        response.getStatus().setMessage("Consumption EndPoints");
        return response;
    }
    
    @PostMapping("/calls")
    public ResponseBody consumeCalls(@RequestBody CallJSON _call) {
        ResponseBody response = new ResponseBody();
            Connection conn = null;
            try {
                conn = ConnGen.getConn();                
                new Customer().makeCall(_call, purchaseRepository, conn);
                response.getStatus().setMessage("Succés");
            } catch(Exception ex) {
                this.setError(response, ex);
                out(ex);
            } finally {
                try { if(conn!=null)conn.close();} catch(SQLException ex) {out(ex);}
            }
            return response;
    }
    
    @PostMapping("/messages")
    public ResponseBody consumeMessages(@RequestBody MessageJSON _message) {
         ResponseBody response = new ResponseBody();
        Connection conn = null;
        try {
            conn = ConnGen.getConn();                
            new Customer().sendMessage(_message, purchaseRepository, conn);
            response.getStatus().setMessage("Succés");
        } catch(IllegalAccessException | IllegalArgumentException | InstantiationException | NoSuchMethodException | InvocationTargetException | SQLException | ParseException | InvalidAmountException | InvalidDateException | NotFoundException ex) {
            this.setError(response, ex);
            out(ex);
        } finally {
            try { if(conn!=null)conn.close();} catch(SQLException ex) {out(ex);}
        }
        return response;
    }
    
    @PostMapping("/internet")
    public ResponseBody consumeInternet(@RequestBody InternetJSON _internet) {
        ResponseBody response = new ResponseBody();
        Connection conn = null;
        try {
            conn = ConnGen.getConn();
            new Customer().useInternet(_internet, purchaseRepository, conn);
            response.getStatus().setMessage("Succés");
        } catch(Exception ex) {
            setError(response, ex);
            out(ex);
        }
        return response;
    }
}
