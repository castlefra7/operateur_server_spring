/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mg.operateur.web_services.resources.commons.offer;

import java.util.Objects;
import mg.operateur.business_logic.offer.Application;
import mg.operateur.business_logic.offer.Unit;

/**
 *
 * @author dodaa
 */
public class ApplicationJSON {
    private int id;
    private String name;
    private char t_type;
    private int internet_application_id;
    private UnitJSON unit;

    public int getId() {
        return id;
    }

    public int getInternet_application_id() {
        return internet_application_id;
    }

    public void setInternet_application_id(int internet_application_id) {
        this.internet_application_id = internet_application_id;
    }

    public char getT_type() {
        return t_type;
    }

    public void setT_type(char t_type) {
        this.t_type = t_type;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) throws Exception {
        this.name = name;
    }
    
    public UnitJSON getUnit() {
        return unit;
    }

    public void setUnit(UnitJSON unit) throws Exception {
        this.unit = unit;
    }
}
