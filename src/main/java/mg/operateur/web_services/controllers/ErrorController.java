/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mg.operateur.web_services.controllers;

import mg.operateur.web_services.ResponseBody;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author lacha
 */
@CrossOrigin(origins = "*")
@RequestMapping("/errors")
@RestController()
public class ErrorController {

    @GetMapping()
    public ResponseBody index() {
        ResponseBody response = new ResponseBody();
        response.getStatus().setCode(500);
        response.getStatus().setMessage("Une erreur s'est produite");
        return response;
    }

    @RequestMapping("/tokens")
    public ResponseBody tokensError() {
        ResponseBody response = new ResponseBody();
        response.getStatus().setCode(500);
        response.getStatus().setMessage("Spécifier un token");
        return response;
    }
    
    
    @RequestMapping("/invalidtokens")
    public ResponseBody invalidTokens() {
        ResponseBody response = new ResponseBody();
        response.getStatus().setCode(500);
        response.getStatus().setMessage("Votre token est invalide");
        return response;
    }
    
    @RequestMapping("/notadmin")
    public ResponseBody notAuthorized() {
        ResponseBody response = new ResponseBody();
        response.getStatus().setCode(500);
        response.getStatus().setMessage("Vous n'avez pas l'autorisation");
        return response;
    }
}
