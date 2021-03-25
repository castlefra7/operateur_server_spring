/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mg.operateur.business_logic.offer;

import mg.operateur.gen.InvalidFormatException;
import mg.operateur.gen.RequiredException;

/**
 *
 * @author dodaa
 */
public class Unit {
    private int id;
    private String suffix;

    public Unit() {
    }

    public Unit(int id, String suffix) throws RequiredException, InvalidFormatException {
        setId(id);
        setSuffix(suffix);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSuffix() {
        return suffix;
    }

    public void setSuffix(String suffix) throws RequiredException, InvalidFormatException {
        if (suffix == null)
            throw new RequiredException("Unit suffix is required");
        suffix = suffix.toLowerCase();
        if(suffix != "sec" && suffix != "mn"  && suffix != "hr" && suffix != "n" && suffix != "ko" && suffix != "go" && suffix != "mo") throw new InvalidFormatException("L'unité doit être: Mn, Hr, Sec, Go, Ko, Mo");
        this.suffix = suffix;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Unit other = (Unit) obj;
        if (this.id != other.id) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Unit{" + "\t\nid=" + id + ", \t\nsuffix=" + suffix + '}';
    }
    
}
