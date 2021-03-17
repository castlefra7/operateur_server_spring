/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mg.operateur.web_services.controllers;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import mg.operateur.business_logic.mobile_credit.Credit;
import mg.operateur.business_logic.mobile_credit.Customer;
import mg.operateur.business_logic.offer.Offer;
import mg.operateur.conn.ConnGen;
import mg.operateur.gen.CDate;
import mg.operateur.web_services.ResponseBody;
import mg.operateur.web_services.resources.commons.TransferJSON;
import mg.operateur.web_services.resources.credit.CreditJSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Service;
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
public class OfferController {
    private static final String prefix = "/offers";

            @Autowired
    private OfferRepository repository;
    
    private void out(Exception ex) {
        ex.printStackTrace();
        System.out.println(ex.getMessage());
    }
        
    private void setError(ResponseBody response, Exception ex) {
        response.getStatus().setCode(500);
        response.getStatus().setMessage(ex.getMessage());
    }
    
    @GetMapping(prefix + "/ping")
    public ResponseBody index() {
        ResponseBody response = new ResponseBody();
       response.getStatus().setMessage("Offer endpoint");
        return response;
    }
    
    @PostMapping(prefix)
    public ResponseBody create(@RequestBody Offer _offer) {
        ResponseBody response = new ResponseBody();
        Connection conn = null;
        try {
            // function checks()
            repository.save(_offer);
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
    
    @GetMapping(prefix)
    public ResponseBody read() {
        ResponseBody response = new ResponseBody();
        Connection conn = null;
        try {
//            List<Offer> allOffers = repository.findAll();
//            response.setData(allOffers);
//            response.getStatus().setMessage("Ok");
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
