/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mg.operateur.web_services.resources.mobilemoney;

import mg.operateur.web_services.resources.commons.TransacJSON;

/**
 *
 * @author lacha
 */
public class WithdrawJSON extends TransacJSON {

    private String password;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    
    
    @Override
    public String toString() {
        return String.format("%s; %s}", super.toString(), getPassword());
    }
}
