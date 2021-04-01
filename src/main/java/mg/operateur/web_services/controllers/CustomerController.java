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
import mg.operateur.business_logic.mobile_credit.CallsHistory;
import mg.operateur.web_services.ResponseBody;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author lacha
 */
@RequestMapping("/customers")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
public class CustomerController {
    private void out(Exception ex) {
        ex.printStackTrace();
        System.out.println(ex.getMessage());
    }
        
    private void setError(ResponseBody response, Exception ex) {
        response.getStatus().setCode(500);
        response.getStatus().setMessage(ex.getMessage());
    }
    
    @GetMapping("/callshistory")
    public ResponseBody index(@RequestAttribute String id) throws IOException {
        ResponseBody response = new ResponseBody();
    
        try {
            response.getData().add(new CallsHistory().findAll(Integer.valueOf(id)));
            response.getStatus().setMessage("Succés");
        } catch(IllegalAccessException | IllegalArgumentException | InstantiationException | NoSuchMethodException | InvocationTargetException | URISyntaxException | SQLException ex) {
            setError(response, ex);
            out(ex);
        }
        return response;
    }
}
