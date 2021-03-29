/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mg.operateur.web_services.controllers;

import java.sql.Connection;
import java.sql.SQLException;
import mg.operateur.business_logic.mobile_credit.CallsHistory;
import mg.operateur.conn.ConnGen;
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
    public ResponseBody index(@RequestAttribute String id) {
        ResponseBody response = new ResponseBody();
        Connection conn = null;
        try {
            conn = ConnGen.getConn();
            response.getData().add(new CallsHistory().findAll(Integer.valueOf(id), conn));
            response.getStatus().setMessage("Succés");
        } catch(Exception ex) {
            setError(response, ex);
            out(ex);
        } finally {
            try {if(conn!=null) conn.close();}catch(SQLException ex) {setError(response, ex);out(ex);}
        }
        return response;
    }
}
