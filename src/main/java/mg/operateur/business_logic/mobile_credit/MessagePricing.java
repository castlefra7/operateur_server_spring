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
import mg.operateur.conn.ConnGen;
import mg.operateur.gen.CDate;
import mg.operateur.gen.FctGen;
import mg.operateur.gen.InvalidAmountException;
import mg.operateur.web_services.resources.pricings.MessagePricingJSON;

/**
 *
 * @author lacha
 */
public class MessagePricing extends Pricing {

    private int unit;

    public int getUnit() {
        return unit;
    }

    public void setUnit(int unit) throws InvalidAmountException {
        if (unit < 0) {
            throw new InvalidAmountException("Veuillez entrer une unité de message positive");
        }
        this.unit = unit;
    }

    public void insert(MessagePricingJSON _message)
            throws IllegalAccessException, IllegalArgumentException, SQLException, InvocationTargetException, URISyntaxException, ParseException, InvalidAmountException {

        Connection conn = null;
        try {
            conn = ConnGen.getConn();
            MessagePricing pricing = new MessagePricing();
            pricing.setCreated_at(CDate.getDate().parse(_message.getDate()));
            pricing.setAmount_exterior(_message.getAmount_exterior());
            pricing.setAmount_interior(_message.getAmount_interior());
            pricing.setUnit(_message.getUnit());
            pricing.insert(conn);
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

    public void insert(Connection conn)
            throws IllegalAccessException, IllegalArgumentException, SQLException, InvocationTargetException {
        FctGen.insert(this, columns(), tableName(), conn);

    }

    public MessagePricing findLatest()
            throws SQLException, InstantiationException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, URISyntaxException {
        Connection conn = null;
        MessagePricing result = null;
        try {
            conn = ConnGen.getConn();
            MessagePricing pricing = new MessagePricing();
            result = pricing.findLatest(conn);
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

    public MessagePricing findLatest(Connection conn)
            throws SQLException, InstantiationException, NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        return (MessagePricing) FctGen.find(new MessagePricing(), "select * from mg.messages_pricings where created_at in (select max(created_at) from mg.messages_pricings);", columns(), conn);
    }

    @Override
    public String tableName() {
        return "mg.messages_pricings";
    }

    @Override
    public String[] columns() {
        return new String[]{"amount_interior", "amount_exterior", "created_at", "unit"};
    }
}
