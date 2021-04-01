/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mg.operateur.business_logic.mobile_credit;

import java.io.IOException;
import mg.operateur.gen.FctGen;
import java.lang.reflect.InvocationTargetException;
import java.net.URISyntaxException;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import mg.operateur.conn.ConnGen;
import mg.operateur.gen.CDate;
import mg.operateur.gen.InvalidAmountException;
import mg.operateur.web_services.resources.mobilemoney.FeeJSON;

/**
 *
 * @author lacha
 */
public class Fee extends BaseClass {

    public List<Object> getAllFees(Connection conn) throws SQLException, IllegalAccessException, InstantiationException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException {
        return FctGen.findAll(new Fee(), String.format("select * from %s", tableName()), columns(), conn);
    }

    public Fee getLastFee(Date date, Connection conn) throws SQLException, InstantiationException, NoSuchMethodException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        return (Fee) FctGen.find(new Fee(), String.format(" select * from %s where created_at <= '%s' order by created_at desc limit 1", tableName(), date.toString()), columns(), conn);
    }

    public void insert(FeeJSON _fee) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, SQLException, URISyntaxException, ParseException, InvalidAmountException, IOException {
        Connection conn = null;
        try {
            conn = ConnGen.getConn();
            Fee fee = new Fee();
            fee.setCreated_at(CDate.getDate().parse(_fee.getDate()));
            fee.setAmount_max(_fee.getAmount_max());
            fee.setAmount_min(_fee.getAmount_min());
            fee.setAmount_fee(_fee.getAmount_fee());

            fee.insert(conn);
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | URISyntaxException | SQLException | ParseException | InvalidAmountException ex) {
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

    public void insert(Connection conn) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, SQLException {
        FctGen.insert(this, columns(), tableName(), conn);
    }

    @Override
    public String tableName() {
        return "mg.fees";
    }

    @Override
    public String[] columns() {
        return new String[]{"created_at", "amount_min", "amount_max", "amount_fee"};
    }

    /* PROPERTIES */
    private Date created_at;
    private double amount_min;
    private double amount_max;
    private double amount_fee;

    public Date getCreated_at() {
        return created_at;
    }

    public void setCreated_at(Date created_at) {
        this.created_at = created_at;
    }

    public double getAmount_min() {
        return amount_min;
    }

    public void setAmount_min(double amount_min) throws InvalidAmountException {
        if (amount_min < 0) {
            throw new InvalidAmountException();
        }
        this.amount_min = amount_min;
    }

    public double getAmount_max() {
        return amount_max;
    }

    public void setAmount_max(double amount_max) throws InvalidAmountException {
        if (amount_max < 0) {
            throw new InvalidAmountException();
        }
        this.amount_max = amount_max;
    }

    public double getAmount_fee() {
        return amount_fee;
    }

    public void setAmount_fee(double amount_fee) throws InvalidAmountException {
        if (amount_fee < 0) {
            throw new InvalidAmountException();
        }
        this.amount_fee = amount_fee;
    }

}
