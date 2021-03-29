/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mg.operateur.web_services.controllers;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import mg.operateur.business_logic.offer.Application;
import mg.operateur.conn.ConnGen;
import mg.operateur.web_services.ResponseBody;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author lacha
 */
@RequestMapping("/apps")
@CrossOrigin(origins = "*")
@RestController
public class InternetApplicationController {
    
    
    private void out(Exception ex) {
        ex.printStackTrace();
        System.out.println(ex.getMessage());
    }
        
    private void setError(ResponseBody response, Exception ex) {
        response.getStatus().setCode(500);
        response.getStatus().setMessage(ex.getMessage());
    }
    
    @RequestMapping("/ping")
    public ResponseBody index() {
        ResponseBody response = new ResponseBody();
       response.getStatus().setMessage("Applications endpoint");
        return response;
    }
    
     @GetMapping("/internet")
    public ResponseBody readInternetApps() {
        ResponseBody response = new ResponseBody();
        Connection conn = null;
        try {
            conn = ConnGen.getConn();
            List<Object> internet_applications = new Application().findAllInternetApps(conn);
            response.setData(internet_applications);
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
    
    @GetMapping()
    public ResponseBody read() {
        ResponseBody response = new ResponseBody();
        Connection conn = null;
        try {
            conn = ConnGen.getConn();
            List<Application> internet_applications = new Application().findAll(conn);
            response.getData().add(internet_applications);
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
