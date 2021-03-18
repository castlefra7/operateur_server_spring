/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mg.operateur.web_services.controllers;

import mg.operateur.web_services.ResponseBody;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author lacha
 */
@CrossOrigin(origins = "*")
@RestController
public class ConsumptionController {
    private static final String prefix = "/consumptions";
    
    
    @GetMapping(prefix)
    public ResponseBody index() {
        ResponseBody response = new ResponseBody();
        response.getStatus().setMessage("Consumption EndPoints");
        return response;
    }
    
    @PostMapping(prefix+"/calls")
    public ResponseBody consumeCalls(@RequestBody String _req) {
        ResponseBody response = new ResponseBody();
        // TODO
        return response;
    }
    
    @PostMapping(prefix+"/messages")
    public ResponseBody consumeMessages(@RequestBody String _req) {
        ResponseBody response = new ResponseBody();
        // TODO
        return response;
    }
    
    @PostMapping(prefix+"/internet")
    public ResponseBody consumeInternet(@RequestBody String _req) {
        ResponseBody response = new ResponseBody();
        // TODO
        return response;
    }
}
