/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mg.operateur.business_logic.mobile_credit;

import java.lang.reflect.InvocationTargetException;
import java.net.URISyntaxException;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.Date;
import mg.operateur.conn.ConnGen;
import mg.operateur.gen.CDate;
import mg.operateur.gen.FctGen;
import mg.operateur.gen.InvalidAmountException;
import mg.operateur.web_services.resources.pricings.InternetPricingJSON;

/**
 *
 * @author dodaa
 */
public class InternetPricing {

    private Date created_at;
    private double amount;

    public int convertToKo(String amount) throws InvalidAmountException {
        amount = amount.toLowerCase();
        if (amount.length() < 3) {
            throw new InvalidAmountException("Vérifier la consommation entrée");
        }
        double r = Double.valueOf(amount.substring(0, amount.length() - 2));
        System.out.println(r);
        int result = (int) r;
        if (amount.endsWith("mo")) {
            result *= 1000;
        } else if (amount.endsWith("ko")) {
        } else if (amount.endsWith("go")) {
            result *= 1000000;
        } else {
            throw new InvalidAmountException("Vérifier la consommation entrée");
        }
        return result;
    }

    public Date getCreated_at() {
        return created_at;
    }

    public void setCreated_at(Date created_at) {
        this.created_at = created_at;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) throws InvalidAmountException {
        if (amount < 0) {
            throw new InvalidAmountException("Veuillez entrer un prix d'internet Positif");
        }
        this.amount = amount;
    }

    public void insert(InternetPricingJSON _internet)
            throws IllegalAccessException, InvocationTargetException, SQLException, ParseException, InvalidAmountException, URISyntaxException {
        Connection conn = null;
        try {
            conn = ConnGen.getConn();
            InternetPricing pricing = new InternetPricing();
            pricing.setCreated_at(CDate.getDate().parse(_internet.getDate()));
            pricing.setAmount(_internet.getAmount());
            pricing.insert(conn);
        } catch (InvalidAmountException ex) {
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
            throws IllegalAccessException, InvocationTargetException, SQLException {
        FctGen.insert(this, columns(), tableName(), conn);
    }

    public String tableName() {
        return "mg.internet_pricings";
    }

    public String[] columns() {
        return new String[]{"created_at", "amount"};
    }

    public InternetPricing getLastPricing() throws SQLException, InstantiationException, NoSuchMethodException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, URISyntaxException {
        Connection conn = null;
        InternetPricing result = null;
        try {
            conn = ConnGen.getConn();
            InternetPricing pricing = new InternetPricing();
            result = pricing.getLastPricing(conn);
        } catch (IllegalAccessException | IllegalArgumentException | InstantiationException | NoSuchMethodException | InvocationTargetException | URISyntaxException | SQLException ex) {
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

    public InternetPricing getLastPricing(Connection conn) throws SQLException, InstantiationException, NoSuchMethodException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        String req = String.format("select * from %s order by created_at desc limit 1", tableName());
        Object ob = FctGen.find(this, req, columns(), conn);
        if (ob == null) {
            throw new NullPointerException("Pas encore de prix pour cette opération");
        }
        return (InternetPricing) ob;
    }
}
