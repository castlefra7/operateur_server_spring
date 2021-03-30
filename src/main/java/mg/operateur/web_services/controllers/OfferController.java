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
import mg.operateur.business_logic.mobile_credit.Customer;
import mg.operateur.business_logic.offer.Amount;
import mg.operateur.business_logic.offer.Application;
import mg.operateur.business_logic.offer.Limitation;
import mg.operateur.business_logic.offer.Offer;
import mg.operateur.business_logic.offer.Price;
import mg.operateur.business_logic.offer.Unit;
import mg.operateur.business_logic.offer.Utilization;
import mg.operateur.conn.ConnGen;
import mg.operateur.gen.CDate;
import mg.operateur.web_services.ResponseBody;
import mg.operateur.web_services.resources.commons.AskJSON;
import mg.operateur.web_services.resources.commons.TransacJSON;
import mg.operateur.web_services.resources.offer.AmountJSON;
import mg.operateur.web_services.resources.offer.OfferJSON;
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
@CrossOrigin(origins = "*", allowedHeaders = "*")
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
    
    @PostMapping(value = "/buyfrommobile")
    public ResponseBody buyOfferMobileMOney(
            @RequestBody TransacJSON _purchase
    ) {
        ResponseBody response = new ResponseBody();
        Connection conn = null;
        try {
            conn = ConnGen.getConn();
            new Offer().buyFromMobileMoney(_purchase, offerRepository, purchaseRepository, conn);
            response.getStatus().setMessage("Succés");
        } catch(Exception ex) {
            setError(response, ex);
            out(ex);
        } finally {
            try {if(conn!=null) conn.close();}catch(SQLException ex) {setError(response, ex);out(ex);}
        }
        return response;
    }
    
        
    @PostMapping(value = "/buy")
    public ResponseBody buyFromCredit(
            @RequestBody TransacJSON _purchase
    ) {
        ResponseBody response = new ResponseBody();
        Connection conn = null;
        try {
            conn = ConnGen.getConn();
            new Offer().buy(_purchase, offerRepository, purchaseRepository, conn);
            response.getStatus().setMessage("Succés");
        } catch(Exception ex) {
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
            if(offerRepository.findByCode(_offer.getCode()) != null) throw new Exception("Veuillez choisir un autre code");

            conn = ConnGen.getConn();
            Limitation limitation = new Limitation(_offer.getLimitation().getBuyingLimit(), _offer.getLimitation().getDurationInDays());
            ArrayList<Amount> amounts = new ArrayList<Amount>();
            for (int i = 0; i < _offer.getAmounts().size(); i++) {
                
                AmountJSON amountJSON = _offer.getAmounts().get(i);
                Unit unit = new Unit(amountJSON.getApplication().getUnit().getId(), amountJSON.getApplication().getUnit().getSuffix());
                Application application = new Application(amountJSON.getApplication().getId(), amountJSON.getApplication().getName(), amountJSON.getApplication().getT_type(), amountJSON.getApplication().getInternet_application_id(), unit);
                Amount amount = new Amount(application, amountJSON.getValue());
                
                if (amountJSON.getUtilization() != null) {
                    Price intra = new Price(amountJSON.getUtilization().getIntra().getPrice(), amountJSON.getUtilization().getIntra().getPer());
                    Price extra = new Price(amountJSON.getUtilization().getExtra().getPrice(), amountJSON.getUtilization().getExtra().getPer());
                    amount.setUtilization(new Utilization(intra, extra));
                }
                
                amounts.add(amount);
            }
            
            int lastId = Offer.getLastId(conn);
            Offer offer = new Offer(lastId, _offer.getName(), CDate.getDate().parse(_offer.getCreatedAt()), _offer.getPrice(), _offer.getValidityDay(), limitation, amounts, _offer.getPriority());
            offer.setIsOneDay(_offer.getIsOneDay());
            offer.setCode(_offer.getCode());
            
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
    
    @GetMapping("/remainings")
    public ResponseBody response(@RequestBody AskJSON _ask) {
        ResponseBody response = new ResponseBody();
        try {
            response.getData().add(new Customer().getRemainings(_ask, purchaseRepository));
        } catch(Exception ex) {
            setError(response, ex);
            out(ex);
        }
        return response;
    }
}
