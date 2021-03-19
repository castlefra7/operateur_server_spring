/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mg.operateur.web_services.controllers;

import java.sql.Connection;
import java.sql.SQLException;
import mg.operateur.business_logic.mobile_credit.Credit;
import mg.operateur.business_logic.mobile_credit.Customer;
import mg.operateur.conn.ConnGen;
import mg.operateur.gen.CDate;
import mg.operateur.web_services.ResponseBody;
import mg.operateur.web_services.resources.commons.AskJSON;
import mg.operateur.web_services.resources.commons.TransferJSON;
import mg.operateur.web_services.resources.commons.MessageJSON;
import mg.operateur.web_services.resources.credit.CreditJSON;
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
@CrossOrigin(origins = "*")
@RestController
public class CreditController {
    private static final String prefix = "/credit";
    
    private void out(Exception ex) {
        ex.printStackTrace();
        System.out.println(ex.getMessage());
    }
        
    private void setError(ResponseBody response, Exception ex) {
        response.getStatus().setCode(500);
        response.getStatus().setMessage(ex.getMessage());
    }
    
    @RequestMapping(prefix)
    public ResponseBody index() {
        ResponseBody response = new ResponseBody();
       response.getStatus().setMessage("Crédit endpoint");
        return response;
    }
    
    @GetMapping(prefix+"/balance")
    public ResponseBody balance( @RequestBody AskJSON _ask) {
        ResponseBody response = new ResponseBody();
        Connection conn = null;
        try {
            conn = ConnGen.getConn();
            double balance = new Customer(_ask.getCustomer_id()).creditBalance(CDate.getDate().parse(_ask.getDate()), conn);
            response.getData().add(new MessageJSON("Solde de votre crédit est de " + balance));
        } catch(Exception ex) {
            setError(response, ex);
            out(ex);
        } finally {
            try {if(conn!=null) conn.close();}catch(SQLException ex) {setError(response, ex);out(ex);}
        }
        
        return response;
    }
    
    @PostMapping(prefix+"/buy")
    public ResponseBody buy(@RequestBody CreditJSON _credit) {
        ResponseBody response = new ResponseBody();
        Connection conn = null;
        try {
            conn = ConnGen.getConn();
            Customer customer = new Customer().find(_credit.getPhone_number(), conn);
            
            Credit credit = new Credit();
            credit.setCustomer_id(customer.getId());
            credit.setAmount(_credit.getAmount());
            credit.setCustomer_source_id(customer.getId());
            credit.setCreated_at(CDate.getDate().parse(_credit.getDate()));
            
            customer.buyCredit(credit, conn);
        } catch(Exception ex) {
            setError(response, ex);
            out(ex);
        } finally {
            try {if(conn!=null) conn.close();}catch(SQLException ex) {setError(response, ex);out(ex);}
        }
        return response;
    }
    
    @PostMapping(prefix+"/transfer")
    public ResponseBody transfer(@RequestBody TransferJSON _transfer) {
        ResponseBody response = new ResponseBody();
        Connection conn = null;
        try {
            conn = ConnGen.getConn();
            Customer customer = new Customer().find(_transfer.getPhone_number(), conn);
            Customer customerDest = new Customer().find(_transfer.getPhone_number_destination(), conn);
            
            customer.transferCredit(_transfer.getAmount(), _transfer.getPassword(), CDate.getDate().parse(_transfer.getDate()), customerDest.getId(), conn);
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
