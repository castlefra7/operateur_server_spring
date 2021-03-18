/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mg.operateur.web_services.controllers;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import mg.operateur.business_logic.offer.Amount;
import mg.operateur.business_logic.offer.Application;
import mg.operateur.business_logic.offer.Limitation;
import mg.operateur.business_logic.offer.Offer;
import mg.operateur.business_logic.offer.Unit;
import mg.operateur.conn.ConnGen;
import mg.operateur.web_services.ResponseBody;
import mg.operateur.web_services.resources.commons.offer.AmountJSON;
import mg.operateur.web_services.resources.commons.offer.OfferJSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author lacha
 */
@CrossOrigin(origins = "*")
@RestController
public class OfferController {
    private static final String prefix = "/offers";

    @Autowired
    private OfferRepository repository;
    
    private void out(Exception ex) {
        ex.printStackTrace();
        System.out.println(ex.getMessage());
    }
        
    private void setError(ResponseBody response, Exception ex) {
        response.getStatus().setCode(500);
        response.getStatus().setMessage(ex.getMessage());
    }
    
    @GetMapping(prefix + "/ping")
    public ResponseBody index() {
        ResponseBody response = new ResponseBody();
       response.getStatus().setMessage("Offer endpoint");
        return response;
    }
    
    @PostMapping(prefix)
    public ResponseBody create(@RequestBody OfferJSON _offer) {
        ResponseBody response = new ResponseBody();
        Connection conn = null;
        try {
            conn = ConnGen.getConn();
            // function checks()
            Limitation limitation = new Limitation(_offer.getLimitation().getBuyingLimit(), _offer.getLimitation().getDurationInDays());
            ArrayList<Amount> amounts = new ArrayList<Amount>();

            for (int i = 0; i < _offer.getAmounts().size(); i++) {
                AmountJSON amountJSON = _offer.getAmounts().get(i);
                Unit unit = new Unit(amountJSON.getApplication().getUnit().getId(), amountJSON.getApplication().getUnit().getSuffix());
                Application application = new Application(amountJSON.getApplication().getId(), amountJSON.getApplication().getName(), unit);
                amounts.add(new Amount(application, amountJSON.getValue()));
            }
            
            int lastId = Offer.getLastId(conn);
            Offer offer = new Offer(lastId, _offer.getName(), _offer.getCreatedAt(), _offer.getPrice(), _offer.getValidityDay(), limitation, amounts, _offer.getPriority());
            repository.save(offer);
            response.getStatus().setMessage("Offer Created");
        } catch(Exception ex) {
            setError(response, ex);
            out(ex);
        } finally {
             try {
                    if(conn!=null) conn.close();
                }  catch(SQLException ex) {
                    setError(response, ex);
                    out(ex);
                }
        }
        return response;
    }
    
    @GetMapping(prefix)
    public ResponseBody read() {
        ResponseBody response = new ResponseBody();
        try {
            List<Offer> allOffers = repository.findAll();
            ArrayList<Object> list = new ArrayList<Object>();
            allOffers.forEach(o -> list.add(o));
            response.setData(list);
            response.getStatus().setMessage("Ok");
        } catch(Exception ex) {
            setError(response, ex);
            out(ex);
        } finally {
            
        }
        return response;
    }
}
