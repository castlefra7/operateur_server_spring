/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mg.operateur.web_services.controllers;

import java.sql.Connection;
import java.sql.SQLException;
import mg.operateur.business_logic.mobile_credit.Customer;
import mg.operateur.conn.ConnGen;
import mg.operateur.web_services.ResponseBody;
import mg.operateur.web_services.resources.consumptions.CallJSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author lacha
 */
@RestController
public class TestController {
    @Autowired
    PurchaseRepository purchaseRepository;
    
    private static final String prefix = "/test";
    private void out(Exception ex) {
            ex.printStackTrace();
            System.out.println(ex.getMessage());
        }
        
        private void setError(ResponseBody response, Exception ex) {
            response.getStatus().setCode(500);
            response.getStatus().setMessage(ex.getMessage());
        }
        
    @GetMapping("/error")
    public ResponseBody error() {
        ResponseBody response = new ResponseBody();
        response.getStatus().setCode(404);
        response.getStatus().setMessage("Ressource introuvable");
        return response;
    }
    
    @RequestMapping("/test")
	public ResponseBody index() {
            ResponseBody response = new ResponseBody();
            Connection conn = null;
            try {
                response.getStatus().setMessage("Succés");
            } catch(Exception ex) {
                this.setError(response, ex);
                out(ex);
            } finally {
                try { if(conn!=null)conn.close();} catch(SQLException ex) {out(ex);}
            }
            return response;
	}
}


class AA {
    private double montant;
    
    public AA(double _montant) {
        this.setMontant(_montant);
    }

    public double getMontant() {
        return montant;
    }

    public void setMontant(double montant) {
        this.montant = montant;
    }
    
}