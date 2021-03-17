/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mg.operateur.web_services.resources.commons;

import mg.operateur.web_services.resources.commons.TransacJSON;

/**
 *
 * @author lacha
 */
public class TransferJSON extends TransacJSON {
    private String password;
    private String phone_number_destination;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone_number_destination() {
        return phone_number_destination;
    }

    public void setPhone_number_destination(String phone_number_destination) {
        this.phone_number_destination = phone_number_destination;
    }
    
    @Override
    public String toString() {
        return String.format("%s; %s; %s}", super.toString(), this.getPassword(), this.getPhone_number_destination());
    }
    
}
