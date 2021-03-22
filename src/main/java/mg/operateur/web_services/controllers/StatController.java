/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mg.operateur.web_services.controllers;

import mg.operateur.business_logic.statistics.Statistic;
import mg.operateur.gen.CDate;
import mg.operateur.web_services.ResponseBody;
import mg.operateur.web_services.resources.commons.AskJSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author lacha
 */
@RequestMapping("/stats")
@CrossOrigin(origins = "*")
@RestController
public class StatController {
    
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
    public ResponseBody index(@RequestBody AskJSON _ask) {
        ResponseBody response = new ResponseBody();
        try {
            response.getData().add(new Statistic().getOffersStat(CDate.getDate().parse(_ask.getDate()), purchaseRepository));
        } catch(Exception ex) {
            setError(response, ex);
            out(ex);
        }
        
        return response;
    }
}
