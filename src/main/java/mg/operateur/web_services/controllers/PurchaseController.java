/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mg.operateur.web_services.controllers;

import java.sql.Connection;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import mg.operateur.business_logic.mobile_credit.Customer;
import mg.operateur.business_logic.offer.Account;
import mg.operateur.business_logic.offer.Offer;
import mg.operateur.business_logic.offer.Purchase;
import mg.operateur.conn.ConnGen;
import mg.operateur.web_services.ResponseBody;
import mg.operateur.web_services.resources.commons.TransacJSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author lacha
 */
@CrossOrigin(origins = "*")
@RestController
public class PurchaseController {
    
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
    
    @RequestMapping("purchases/ping")
    public ResponseBody index() {
        ResponseBody response = new ResponseBody();
       response.getStatus().setMessage("Purchase endpoint");
        return response;
    }
    
    @PostMapping(value = "offers/{offer_id}/buy")
    public ResponseBody create(
            @PathVariable("offer_id") int _offerId, 
            @RequestBody TransacJSON _purchase
    ) {
        ResponseBody response = new ResponseBody();
        Connection conn = null;
        try {
            conn = ConnGen.getConn();
            Customer foundCustomer = new Customer().find(_purchase.getPhone_number(), conn);
//            
            Offer offer = offerRepository.findById(_offerId);
            if (offer == null)
                throw new Exception("L'offre specifié n'existe pas");
//            
            mg.operateur.business_logic.offer.Customer customer = new mg.operateur.business_logic.offer.
                    Customer(foundCustomer.getId(), foundCustomer.getName(), foundCustomer.getEmail(), "");
//            
//            List<Purchase> purchases = Purchase.findByCustomerId(foundCustomer.getId(), conn);
            List<Purchase> purchases = purchaseRepository.findByCustomer_id(foundCustomer.getId());
//            
            purchases.forEach(p -> {
                try {
                    p.setOffer(offerRepository.findById(p.getOffer_id()));
                } catch (Exception ex) {
                    Logger.getLogger(PurchaseController.class.getName()).log(Level.SEVERE, null, ex);
                }
            });
//            
            Account account = new Account(foundCustomer.getId(), purchases, new ArrayList<>());
            customer.setAccount(account);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            customer.purchase(offer, sdf.parse(_purchase.getDate()), conn, purchaseRepository);
//            
            response.getStatus().setMessage(String.valueOf("ok"));
//             response.getData().add(offer);
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
}
