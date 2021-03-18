/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mg.operateur.web_services.controllers;

import java.sql.Connection;
import java.sql.SQLException;
import mg.operateur.business_logic.mobile_credit.Deposit;
import mg.operateur.business_logic.mobile_credit.DepositView;
import mg.operateur.conn.ConnGen;
import mg.operateur.web_services.ResponseBody;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author lacha
 */
@CrossOrigin(origins = "*")
@RestController
public class POSController {
    private static final String prefix =  "/pos";
    
    private void out(Exception ex) {
        ex.printStackTrace();
        System.out.println(ex.getMessage());
    }
        
    private void setError(ResponseBody response, Exception ex) {
        response.getStatus().setCode(500);
        response.getStatus().setMessage(ex.getMessage());
    }
    
    
    @GetMapping(prefix)
    public ResponseBody index() {
        ResponseBody response = new ResponseBody();
        response.getStatus().setMessage("POS endpoint");
        return response;
    }
    
    @PutMapping(prefix+"/validate/{id}")
    public ResponseBody validateDeposit(@PathVariable(value="id")int id) {
        ResponseBody response = new ResponseBody();
        Connection conn= null;
        try {
            conn = ConnGen.getConn();
            Deposit deposit = new Deposit(id); /// GETID
            deposit.validate(conn);
            response.getStatus().setMessage("Succés");
        } catch(Exception ex) {
            setError(response, ex);
            out(ex);
        } finally {
            try { if(conn!=null)conn.close();} catch(SQLException ex) {out(ex);}
        }
        return response;
    }
    
    @GetMapping(prefix+"/deposits")
    public ResponseBody allNonValidatedDeposits() {
        ResponseBody response = new ResponseBody();
        Connection conn= null;
        try {
            conn = ConnGen.getConn();
            response.setData(new DepositView().findAllDeposit(conn));
            response.getStatus().setMessage("Succés");
        } catch(Exception ex) {
            setError(response, ex);
            out(ex);
        } finally {
            try { if(conn!=null)conn.close();} catch(SQLException ex) {out(ex);}
        }
        return response;
    }
}
