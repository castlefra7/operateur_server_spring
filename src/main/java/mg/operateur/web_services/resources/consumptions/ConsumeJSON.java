/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mg.operateur.web_services.resources.consumptions;

/**
 *
 * @author lacha
 */
public class ConsumeJSON {
       private String phone_number_source;
    private String phone_number_destination;
    private String date;

    public String getPhone_number_source() {
        return phone_number_source;
    }

    public void setPhone_number_source(String phone_number_source) {
        this.phone_number_source = phone_number_source;
    }

    public String getPhone_number_destination() {
        return phone_number_destination;
    }

    public void setPhone_number_destination(String phone_number_destination) {
        this.phone_number_destination = phone_number_destination;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
    
}
