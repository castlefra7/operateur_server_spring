/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mg.operateur.business_logic.mobile_credit;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URISyntaxException;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.ParseException;
import mg.operateur.conn.ConnGen;
import mg.operateur.gen.CDate;
import mg.operateur.gen.FctGen;
import mg.operateur.gen.InvalidAmountException;
import mg.operateur.web_services.resources.pricings.PricingJSON;

/**
 *
 * @author lacha
 */
public class CallPricing extends Pricing {

    public int convertToSec(String amount) {
        amount = amount.toLowerCase();
        int val = 0;
        return val;
    }

    public void insert(PricingJSON _call)
            throws IllegalAccessException, IllegalAccessException, InvocationTargetException, SQLException, URISyntaxException, ParseException, InvalidAmountException, IOException {
        Connection conn = null;
        try {
            conn = ConnGen.getConn();
            CallPricing pricing = new CallPricing();
            pricing.setCreated_at(CDate.getDate().parse(_call.getDate()));
            pricing.setAmount_exterior(_call.getAmount_exterior());
            pricing.setAmount_interior(_call.getAmount_interior());
            pricing.insert(conn);
        } catch (IllegalAccessException | InvocationTargetException | URISyntaxException | SQLException | ParseException | InvalidAmountException ex) {
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

    public void insert() throws IllegalAccessException, IllegalAccessException, InvocationTargetException, SQLException, URISyntaxException, IOException {
        Connection conn = null;
        try {
            conn = ConnGen.getConn();
            CallPricing pricing = new CallPricing();
            pricing.insert(conn);
        } catch (IllegalAccessException | InvocationTargetException | URISyntaxException | SQLException ex) {
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

    public void insert(Connection conn)
            throws IllegalAccessException, IllegalAccessException, InvocationTargetException, SQLException {
        FctGen.insert(this, columns(), tableName(), conn);

    }

    public CallPricing findLatest()
            throws SQLException, InstantiationException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, URISyntaxException, IOException {
        Connection conn = null;
        CallPricing result = null;
        try {
            conn = ConnGen.getConn();
            result = findLatest(conn);
        } catch (IllegalAccessException | InstantiationException | NoSuchMethodException | InvocationTargetException | URISyntaxException | SQLException ex) {
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
        return result;
    }

    public CallPricing findLatest(Connection conn)
            throws SQLException, InstantiationException, NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        return (CallPricing) FctGen.find(new CallPricing(), "select * from mg.calls_pricings where created_at in (select max(created_at) from mg.calls_pricings)", new String[]{"created_at", "amount_interior", "amount_exterior", "unit"}, conn);
    }

    @Override
    public String tableName() {
        return "mg.calls_pricings";
    }
}
