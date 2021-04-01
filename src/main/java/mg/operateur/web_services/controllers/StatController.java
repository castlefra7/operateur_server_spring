/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mg.operateur.web_services.controllers;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URISyntaxException;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.ParseException;
import mg.operateur.business_logic.statistics.Statistic;
import mg.operateur.conn.ConnGen;
import mg.operateur.gen.CDate;
import mg.operateur.web_services.ResponseBody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author lacha
 */
@RequestMapping("/stats")
@CrossOrigin(origins = "*", allowedHeaders = "*")
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
    public ResponseBody index(
            @RequestParam String date
    ) {
        ResponseBody response = new ResponseBody();
        try {
            response.getData().add(new Statistic().getOffersStat(CDate.getDate().parse(date), purchaseRepository));
        } catch(URISyntaxException | SQLException | ParseException ex) {
            setError(response, ex);
            out(ex);
        }
        
        return response;
    }
    
    @GetMapping("/mobileops")
    public ResponseBody deposits(
    ) throws IOException {
        Connection conn = null;
        ResponseBody response = new ResponseBody();
        try {
            conn = ConnGen.getConn();
            response.getData().add(new Statistic().getDeposits(conn));
            response.getData().add(new Statistic().getWithdraws(conn));
        } catch(IllegalAccessException | IllegalArgumentException | InstantiationException | NoSuchMethodException | InvocationTargetException | URISyntaxException | SQLException ex) {
            setError(response, ex);
            out(ex);
        } finally {
            try { if(conn!=null) conn.close();} catch(SQLException ex) {out(ex); }
        }
        
        return response;
    }
}
