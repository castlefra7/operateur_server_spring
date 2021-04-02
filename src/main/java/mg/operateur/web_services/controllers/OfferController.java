/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mg.operateur.web_services.controllers;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URISyntaxException;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import mg.operateur.business_logic.mobile_credit.Customer;
import mg.operateur.business_logic.mobile_credit.ResponseMessage;
import mg.operateur.business_logic.notifications.NotificationOffer;
import mg.operateur.business_logic.offer.Offer;
import mg.operateur.gen.InvalidAmountException;
import mg.operateur.gen.InvalidDateException;
import mg.operateur.gen.InvalidFormatException;
import mg.operateur.gen.LimitReachedException;
import mg.operateur.gen.NotFoundException;
import mg.operateur.gen.RequiredException;
import mg.operateur.web_services.ResponseBody;
import mg.operateur.web_services.resources.commons.AskJSON;
import mg.operateur.web_services.resources.commons.TransacJSON;
import mg.operateur.web_services.resources.offer.OfferJSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
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
    ) throws IOException {
        ResponseBody response = new ResponseBody();
        try {
            new Offer().buyFromMobileMoney(_purchase, offerRepository, purchaseRepository);
            response.getStatus().setMessage("Succés");
            Offer offer = offerRepository.findByCode(_purchase.getCode());
            response.getData().add(NotificationOffer.notifyPurchaseFromMobileMoney(offer.getName(), _purchase.getDate(), _purchase.getPhone_number()));
        } catch (IllegalAccessException | IllegalArgumentException | InstantiationException | NoSuchMethodException | InvocationTargetException | URISyntaxException | NoSuchAlgorithmException | SQLException | ParseException | InvalidAmountException | InvalidDateException | InvalidFormatException | LimitReachedException | NotFoundException | RequiredException ex) {
            setError(response, ex);
            out(ex);
        }
        return response;
    }

    @PostMapping(value = "/buy")
    public ResponseBody buyFromCredit(
            @RequestBody TransacJSON _purchase
    ) throws IOException {
        ResponseBody response = new ResponseBody();
        try {
            new Offer().buy(_purchase, offerRepository, purchaseRepository);
            response.getStatus().setMessage("Succés");
            Offer offer = offerRepository.findByCode(_purchase.getCode());
            response.getData().add(NotificationOffer.notifyPurchase(offer.getName(), _purchase.getDate(), _purchase.getPhone_number()));
        } catch (IllegalAccessException | IllegalArgumentException | InstantiationException | NoSuchMethodException | InvocationTargetException | URISyntaxException | SQLException | ParseException | InvalidAmountException | InvalidDateException | InvalidFormatException | LimitReachedException | NotFoundException | RequiredException ex) {
            setError(response, ex);
            out(ex);
        }
        return response;
    }

    @PostMapping()
    public ResponseBody create(@RequestBody OfferJSON _offer) {
        ResponseBody response = new ResponseBody();
        try {
            response.getData().add(new Offer().save(_offer, offerRepository));
            response.getStatus().setMessage("Offer Created");
        } catch (Exception ex) {
            setError(response, ex);
            out(ex);
        }
        return response;
    }

    @GetMapping()
    public ResponseBody read() {
        ResponseBody response = new ResponseBody();
        try {
            List<Offer> allOffers = offerRepository.findAll();
            ArrayList<Object> list = new ArrayList<>();
            allOffers.forEach(o -> list.add(o));
            response.setData(list);
            response.getStatus().setMessage("Ok");
        } catch (Exception ex) {
            setError(response, ex);
            out(ex);
        } finally {

        }
        return response;
    }

    @GetMapping("/remainings")
    public ResponseBody response(@RequestBody AskJSON _ask) throws IOException {
        ResponseBody response = new ResponseBody();
        try {
            response.getData().add(ResponseMessage.customerRemainCallsInMess(new Customer().getRemainings(_ask, purchaseRepository)));
        } catch (IllegalAccessException | IllegalArgumentException | InstantiationException | NoSuchMethodException | InvocationTargetException | URISyntaxException | SQLException | ParseException | InvalidAmountException | InvalidFormatException | NotFoundException ex) {
            setError(response, ex);
            out(ex);
        }
        return response;
    }
    
    @GetMapping("/remaingOffers")
    public ResponseBody remainOffersCust(@RequestBody AskJSON _ask) {
        ResponseBody response = new ResponseBody();
        try {
            response.getData().add(ResponseMessage.customerRemainOffer(new Customer().getOffersRemains(_ask, purchaseRepository)));
        } catch(IllegalAccessException | IllegalArgumentException | InstantiationException | NoSuchMethodException | InvocationTargetException | URISyntaxException | SQLException | ParseException | InvalidFormatException | NotFoundException ex) {
            setError(response, ex);
            out(ex);
        }
        return response;
    }
}
