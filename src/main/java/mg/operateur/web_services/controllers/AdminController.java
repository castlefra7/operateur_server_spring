/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mg.operateur.web_services.controllers;

import mg.operateur.web_services.ResponseBody;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author lacha
 */
@CrossOrigin(origins = "*")
@RestController
public class AdminController {
    private static final String prefix = "/admin";
    
    @GetMapping(prefix)
    public ResponseBody index() {
        ResponseBody response = new ResponseBody();
        response.getStatus().setMessage("Admin endpoints");
        return response;
    }
}
