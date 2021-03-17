/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mg.operateur.web_services.controllers;

import org.springframework.data.annotation.Id;

/**
 *
 * @author dodaa
 */
public class OfferBean {

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    @Id
    private String id;
    private String name;

    public OfferBean(){}
    public OfferBean(String id, String name) {
        this.id = id;
        this.name = name;
    }
    
    
    
}
