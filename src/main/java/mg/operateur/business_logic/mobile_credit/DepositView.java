/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mg.operateur.business_logic.mobile_credit;

import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import mg.operateur.gen.FctGen;

/**
 *
 * @author lacha
 */
public class DepositView extends Deposit {
    private String phone_number;
    private String name;

    public List<Object> findAllDeposit(Connection conn) throws SQLException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException {
        List<Object> result = new ArrayList();
        result = FctGen.findAll(new DepositView(), "select * from mg.all_customers_deposits where isvalidated is false and customer_id = customer_source_id", columnsWithID(), conn);
        return result;
    }
    
        public String[] columnsWithID() {
        return new String[]{"id","created_at", "customer_id", "amount", "customer_source_id", "phone_number", "name"};
    }
    
    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
}
