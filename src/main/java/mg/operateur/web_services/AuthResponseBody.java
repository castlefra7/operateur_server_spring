/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mg.operateur.web_services;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author lacha
 */
public class AuthResponseBody extends ResponseBody {
    String token;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
   
    
}
