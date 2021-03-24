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
public class ConfOperator extends BaseClass {
    private Date created_at; 
    private String prefix;
    private String name;
    
    public ConfOperator() {}

    public Date getCreated_at() {
        return created_at;
    }

    public void setCreated_at(Date created_at) {
        this.created_at = created_at;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
    public ConfOperator getLastConf(Connection conn) throws SQLException, InstantiationException, NoSuchMethodException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NotFoundException  {
        ConfOperator result = null;
        String req = String.format("select * from mg.conf_operator order by created_at desc limit 1");
        Object ob = FctGen.find(this, req, columns(), conn);
        if(ob == null) throw new NotFoundException("Veuillez entrer une configuration d'opérateur");
        return (ConfOperator)ob;
    }
    
    
    @Override
    public String tableName() {
        return "mg.conf_operator";
    }

    @Override
    public String[] columns() {
        return new String[] {"created_at", "prefix", "name"};
    }
    
}
