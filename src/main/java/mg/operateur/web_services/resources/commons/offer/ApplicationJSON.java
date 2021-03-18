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
    private UnitJSON unit;

    public int getId() {
        return id;
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
