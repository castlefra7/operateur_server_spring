/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mg.operateur.web_services.controllers;

import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import mg.operateur.business_logic.offer.Amount;
import mg.operateur.business_logic.offer.Application;
import mg.operateur.business_logic.offer.Limitation;
import mg.operateur.business_logic.offer.Offer;
import mg.operateur.business_logic.offer.Price;
import mg.operateur.business_logic.offer.Unit;
import mg.operateur.business_logic.offer.Utilization;
import mg.operateur.conn.ConnGen;
import mg.operateur.gen.InvalidAmountException;
import mg.operateur.gen.InvalidDateException;
import mg.operateur.gen.LimitReachedException;
import mg.operateur.gen.NotFoundException;
import mg.operateur.gen.RequiredException;
import mg.operateur.web_services.ResponseBody;
import mg.operateur.web_services.resources.commons.TransacJSON;
import mg.operateur.web_services.resources.commons.offer.AmountJSON;
import mg.operateur.web_services.resources.commons.offer.OfferJSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author lacha
 */
@RequestMapping("/offers")
@CrossOrigin(origins = "*")
@RestController
public class OfferController {

    @Autowired
    private OfferRepository offerRepository;
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
    
    @GetMapping("/ping")
    public ResponseBody index() {
        ResponseBody response = new ResponseBody();
       response.getStatus().setMessage("Offer endpoint");
        return response;
    }
    
        
    @PostMapping(value = "/{offer_id}/buy")
    public ResponseBody create(
            @PathVariable("offer_id") int _offerId, 
            @RequestBody TransacJSON _purchase
    ) {
        ResponseBody response = new ResponseBody();
        Connection conn = null;
        try {
            conn = ConnGen.getConn();
            new Offer().buy(_offerId, _purchase, offerRepository, purchaseRepository, conn);
            response.getStatus().setMessage("Succés");
        } catch(IllegalAccessException | IllegalArgumentException | InstantiationException | NoSuchMethodException | InvocationTargetException | SQLException | ParseException | InvalidAmountException | InvalidDateException | LimitReachedException | NotFoundException | RequiredException ex) {
            setError(response, ex);
            out(ex);
        } finally {
            try {if(conn!=null) conn.close();}catch(SQLException ex) {setError(response, ex);out(ex);}
        }
        return response;
    }
    
    @PostMapping()
    public ResponseBody create(@RequestBody OfferJSON _offer) {
        ResponseBody response = new ResponseBody();
        Connection conn = null;
        try {
            conn = ConnGen.getConn();
            Limitation limitation = new Limitation(_offer.getLimitation().getBuyingLimit(), _offer.getLimitation().getDurationInDays());
            ArrayList<Amount> amounts = new ArrayList<Amount>();
//
            for (int i = 0; i < _offer.getAmounts().size(); i++) {
                AmountJSON amountJSON = _offer.getAmounts().get(i);
                Unit unit = new Unit(amountJSON.getApplication().getUnit().getId(), amountJSON.getApplication().getUnit().getSuffix());
                Application application = new Application(amountJSON.getApplication().getId(), amountJSON.getApplication().getName(), amountJSON.getApplication().getT_type(), amountJSON.getApplication().getInternet_application_id(), unit);
                Price intra = new Price(amountJSON.getUtilization().getIntra().getPrice(), amountJSON.getUtilization().getIntra().getPer());
                Price extra = new Price(amountJSON.getUtilization().getExtra().getPrice(), amountJSON.getUtilization().getExtra().getPer());
                amounts.add(new Amount(application, amountJSON.getValue(), new Utilization(intra, extra)));
            }
            
            int lastId = Offer.getLastId(conn);
            Offer offer = new Offer(lastId, _offer.getName(), _offer.getCreatedAt(), _offer.getPrice(), _offer.getValidityDay(), limitation, amounts, _offer.getPriority());
            offerRepository.save(offer);
            response.getStatus().setMessage("Offer Created");
                response.getData().add(offer);
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
    
    @GetMapping()
    public ResponseBody read() {
        ResponseBody response = new ResponseBody();
        try {
            List<Offer> allOffers = offerRepository.findAll();
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
