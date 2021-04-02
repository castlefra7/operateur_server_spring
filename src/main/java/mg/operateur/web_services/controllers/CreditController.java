/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mg.operateur.web_services.controllers;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URISyntaxException;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.text.ParseException;
import mg.operateur.business_logic.mobile_credit.Customer;
import mg.operateur.business_logic.notifications.NotificationCredit;
import mg.operateur.gen.InvalidAmountException;
import mg.operateur.gen.InvalidDateException;
import mg.operateur.gen.InvalidFormatException;
import mg.operateur.gen.NotFoundException;
import mg.operateur.gen.RequiredException;
import mg.operateur.web_services.ResponseBody;
import mg.operateur.web_services.resources.commons.AskJSON;
import mg.operateur.web_services.resources.commons.TransferJSON;
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
@RequestMapping("/credits")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
public class CreditController {
    
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
       response.getStatus().setMessage("Crédit endpoint");
        return response;
    }
    
    @GetMapping("/balance")
    public ResponseBody balance( @RequestBody AskJSON _ask) throws IOException {
        ResponseBody response = new ResponseBody();
        try {
            double balance = new Customer().creditBalance(_ask);
            response.getData().add(new mg.operateur.web_services.resources.commons.MessageJSON("Solde de votre crédit est de " + balance));
        } catch(IllegalAccessException | IllegalArgumentException | InstantiationException | NoSuchMethodException | InvocationTargetException | URISyntaxException | SQLException | ParseException | InvalidFormatException | NotFoundException ex) {
            setError(response, ex);
            out(ex);
        }
        return response;
    }
    
    @PostMapping("/buy")
    public ResponseBody buy(@RequestBody CreditJSON _credit) throws IOException {
        ResponseBody response = new ResponseBody();
        try {
            new Customer().buyCredit(_credit);
            response.getData().add(NotificationCredit.notifyCredit(_credit.getAmount(), _credit.getDate(), _credit.getPhone_number()));
        } catch(IllegalAccessException | IllegalArgumentException | InstantiationException | NoSuchMethodException | InvocationTargetException | URISyntaxException | SQLException | ParseException | InvalidAmountException | InvalidDateException | InvalidFormatException | NotFoundException ex) {
            setError(response, ex);
            out(ex);
        }
        return response;
    }
    
    @PostMapping("/transfer")
    public ResponseBody transfer(@RequestBody TransferJSON _transfer) throws IOException {
        ResponseBody response = new ResponseBody();
        try {
           new Customer().transferCredit(_transfer);
           response.getData().add(NotificationCredit.notifyTransfertCredit(_transfer.getAmount(), _transfer.getDate(), _transfer.getPhone_number(), _transfer.getPhone_number_destination()));
        } catch(IllegalAccessException | IllegalArgumentException | InstantiationException | NoSuchMethodException | InvocationTargetException | URISyntaxException | NoSuchAlgorithmException | SQLException | ParseException | InvalidAmountException | InvalidDateException | InvalidFormatException | NotFoundException | RequiredException ex) {
            setError(response, ex);
            out(ex);
        }
        return response;
    }
}
