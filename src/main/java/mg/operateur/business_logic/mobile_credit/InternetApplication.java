/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mg.operateur.business_logic.mobile_credit;

import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;
import mg.operateur.gen.FctGen;
import mg.operateur.gen.NotFoundException;

/**
 *
 * @author lacha
 */
public class InternetApplication extends BaseClass {
    private String name;
    private Date created_at;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getCreated_at() {
        return created_at;
    }

    public void setCreated_at(Date created_at) {
        this.created_at = created_at;
    }
    
    public InternetApplication find(int _id, Connection conn) throws SQLException, InstantiationException, NoSuchMethodException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NotFoundException  {
        InternetApplication result = null;
        Object ob = FctGen.find(this, String.format("select * from mg.internet_applications where id = %d", _id), columns(), conn);
        if(ob == null) throw new NotFoundException("Cette application n'existe pas");
        result = (InternetApplication)ob;
        return result;
    }
    
    @Override
    public String tableName() {
        return "mg.internet_applications";
    }

    @Override
    public String[] columns() {
        return new String[]{"name", "created_at"};
    }
}
