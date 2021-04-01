/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mg.operateur.web_services.controllers;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URISyntaxException;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.Calendar;
import java.util.GregorianCalendar;
import mg.operateur.business_logic.mobile_credit.Customer;
import mg.operateur.conn.ConnGen;
import mg.operateur.gen.InvalidAmountException;
import mg.operateur.gen.InvalidFormatException;
import mg.operateur.gen.NotFoundException;
import mg.operateur.web_services.ResponseBody;
import mg.operateur.web_services.resources.commons.AskJSON;
import mg.operateur.web_services.resources.commons.BalanceJSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author lacha
 */

@RequestMapping("/balances")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
public class BalanceController {

    @Autowired
    private PurchaseRepository purchaseRepository;

    private void out(Exception ex) {
        ex.printStackTrace();
        System.out.println(ex.getMessage());
    }

    private void setError(ResponseBody response, Exception ex) {
        response.getStatus().setCode(500);
        response.getStatus().setMessage(ex.getMessage());
    }

    @GetMapping()
    public ResponseBody index(@RequestParam("phone_number") String _phone) throws IOException {
        ResponseBody response = new ResponseBody();
        Connection conn = null;
        try {
            conn = ConnGen.getConn();
            AskJSON _ask = new AskJSON();
            _ask.setPhone_number(_phone);
            Calendar calendar = new GregorianCalendar();
            if(_ask.getDate() == null) _ask.setDate(String.format("%d-%d-%d", calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)));
            double creditBalance = new Customer().creditBalance(_ask, conn);
            double moneyBalance = new Customer().mobileBalance(_ask, conn);

            response.getData().add(new BalanceJSON(creditBalance, moneyBalance, new Customer().getRemainings(_ask, purchaseRepository)));
            response.getStatus().setMessage("Succés");
        } catch (IllegalAccessException | IllegalArgumentException | InstantiationException | NoSuchMethodException | InvocationTargetException | URISyntaxException | SQLException | ParseException | InvalidAmountException | InvalidFormatException | NotFoundException ex) {
            setError(response, ex);
            out(ex);
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException ex) {
                setError(response, ex);
                out(ex);
            }
        }
        return response;
    }

}
