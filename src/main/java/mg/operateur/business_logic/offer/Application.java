/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mg.operateur.business_logic.offer;

import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import mg.operateur.gen.FctGen;

/**
 *
 * @author dodaa
 */
public class Application {
    private int id;
    private String name;
    private char t_type;
    private int internet_application_id;
    private Unit unit;

    public Application() {
    }

    public Application(int id, String name, Unit unit) throws Exception {   
        setId(id);
        setName(name);
        setUnit(unit);
    }
    
    public Application(int id, String name, char t_type, int internet_application_id, Unit unit) throws Exception {   
        setId(id);
        setName(name);
        setUnit(unit);
        setT_type(t_type);
        setInternet_application_id(internet_application_id);
    }

    public char getT_type() {
        return t_type;
    }

    public void setT_type(char t_type) {
        this.t_type = t_type;
    }

    public int getInternet_application_id() {
        return internet_application_id;
    }

    public void setInternet_application_id(int internet_application_id) {
        this.internet_application_id = internet_application_id;
    }
    
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
        if (name == null)
            throw new Exception("Application name is required");
        this.name = name;
    }
    
    public Unit getUnit() {
        return unit;
    }

    public void setUnit(Unit unit) throws Exception {
        if (unit == null)
            throw new Exception("unit is required");
        this.unit = unit;
    }
    
    public List<Application> findAll(Connection conn) 
            throws SQLException, InstantiationException, IllegalAccessException, InstantiationException, InvocationTargetException, NoSuchMethodException, Exception {
        List<Object> objects = FctGen.findAll(new Application(), "SELECT * FROM mg.internet_applications", columns(), conn);
        ArrayList<Application> val = new ArrayList<Application>();
        objects.forEach(o -> {
            Application app = (Application) o;
            app.setInternet_application_id(app.getId());
            app.setT_type('i');
            val.add(app);
        });
        val.add(new Application(-1, "message", 'm', -1, new Unit()));
        val.add(new Application(-1, "appel", 'c', -1, new Unit()));
        return val;
    }
    
    public String[] columns() {
        return new String[] {"id", "name"};
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
        final Application other = (Application) obj;
        if (this.id != other.id) {
            return false;
        }
        if (!Objects.equals(this.name, other.name)) {
            return false;
        }
        if (!Objects.equals(this.unit, other.unit)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Application{" + "\t\nid=" + id + ", \t\nname=" + name + ", \t\nunit=" + unit + '}';
    }
}
