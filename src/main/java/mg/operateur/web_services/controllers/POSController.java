/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mg.operateur.web_services.controllers;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URISyntaxException;
import java.sql.SQLException;
import mg.operateur.business_logic.mobile_credit.Deposit;
import mg.operateur.business_logic.mobile_credit.DepositView;
import mg.operateur.gen.NotFoundException;
import mg.operateur.web_services.ResponseBody;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author lacha
 */
@RequestMapping("/pos")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
public class POSController {
    
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
        response.getStatus().setMessage("POS endpoint");
        return response;
    }
    
    @PutMapping("/validate/{id}")
    public ResponseBody validateDeposit(@PathVariable(value="id")int id) throws IOException {
        ResponseBody response = new ResponseBody();
        try {
            new Deposit().validate(id);
            response.getStatus().setMessage("Succés");
        } catch(IllegalAccessException | IllegalArgumentException | InstantiationException | NoSuchMethodException | InvocationTargetException | URISyntaxException | SQLException | NotFoundException ex) {
            setError(response, ex);
            out(ex);
        } 
        return response;
    }
    
    @GetMapping("/deposits")
    public ResponseBody allNonValidatedDeposits() throws IOException {
        ResponseBody response = new ResponseBody();
        try {
            response.setData(new DepositView().findAllDeposit());
            response.getStatus().setMessage("Succés");
        } catch(IllegalAccessException | IllegalArgumentException | InstantiationException | NoSuchMethodException | InvocationTargetException | URISyntaxException | SQLException ex) {
            setError(response, ex);
            out(ex);
        }
        return response;
    }
}
