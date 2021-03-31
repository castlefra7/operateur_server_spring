/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mg.operateur.business_logic.mobile_credit;

import mg.operateur.gen.FctGen;
import java.lang.reflect.InvocationTargetException;
import java.net.URISyntaxException;
import java.sql.Connection;
import java.sql.SQLException;
import mg.operateur.conn.ConnGen;
import mg.operateur.gen.NotFoundException;

/**
 *
 * @author lacha
 */
public class Deposit extends Transaction {

    private boolean isValidated;

    public boolean getIsValidated() {
        return isValidated;
    }

    public void setIsValidated(boolean isValidated) {
        this.isValidated = isValidated;
    }

    public Deposit(int _id) {
        this.setId(_id);
    }

    public Deposit() {
    }

    @Override
    public void insert(Connection conn) throws IllegalAccessException, InvocationTargetException, IllegalArgumentException, SQLException {
        FctGen.insert(this, columns(), tableName(), conn);
    }

    public Deposit find(int _id, Connection conn) throws SQLException, InstantiationException, NoSuchMethodException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NotFoundException {

        Object ob = FctGen.find(new Deposit(), "select * from mg.deposits where id = " + _id, new String[]{"amount"}, conn);

        if (ob == null) {
            throw new NotFoundException("Ce dépôt n'existe pas");
        }
        return (Deposit) ob;
    }

    public void validate(int id) throws SQLException, InstantiationException, NoSuchMethodException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NotFoundException, URISyntaxException {
        Connection conn = null;
        try {
            conn = ConnGen.getConn();
            Deposit deposit = new Deposit(id);
            deposit.validate(conn);
        } catch (IllegalAccessException | IllegalArgumentException | InstantiationException | NoSuchMethodException | InvocationTargetException | URISyntaxException | SQLException | NotFoundException ex) {
            throw ex;
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException ex) {
                System.out.println(ex);
            }
        }

    }

    public void validate(Connection conn) throws SQLException, InstantiationException, NoSuchMethodException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NotFoundException {
        find(this.getId(), conn);
        String req = String.format("update mg.deposits set isvalidated = true where id =%d", this.getId());
        FctGen.update(req, conn);
    }

    @Override
    public String tableName() {
        return "mg.deposits";
    }

    @Override
    public String[] columns() {
        return new String[]{"created_at", "customer_id", "amount", "customer_source_id", "isvalidated"};
    }

    /* PROPERTIES */
    private int customer_source_id;

    public int getCustomer_source_id() {
        return customer_source_id;
    }

    public void setCustomer_source_id(int customer_source_id) {
        this.customer_source_id = customer_source_id;
    }

}
