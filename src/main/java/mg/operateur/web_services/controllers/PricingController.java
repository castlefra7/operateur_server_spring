package mg.operateur.web_services.controllers;

import java.lang.reflect.InvocationTargetException;
import java.net.URISyntaxException;
import java.sql.SQLException;
import java.text.ParseException;
import mg.operateur.business_logic.mobile_credit.CallPricing;
import mg.operateur.business_logic.mobile_credit.InternetPricing;
import mg.operateur.business_logic.mobile_credit.MessagePricing;
import mg.operateur.gen.InvalidAmountException;
import mg.operateur.web_services.ResponseBody;
import mg.operateur.web_services.resources.pricings.InternetPricingJSON;
import mg.operateur.web_services.resources.pricings.MessagePricingJSON;
import mg.operateur.web_services.resources.pricings.PricingJSON;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author lacha
 */
@RequestMapping("/pricings")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
public class PricingController {

    private void out(Exception ex) {
        ex.printStackTrace();
        System.out.println(ex.getMessage());
    }

    private void setError(ResponseBody response, Exception ex) {
        response.getStatus().setCode(500);
        response.getStatus().setMessage(ex.getMessage());
    }

    @GetMapping()
    public ResponseBody index() {
        ResponseBody response = new ResponseBody();
        response.getStatus().setMessage("Tarifs EndPoint");
        return response;
    }

    @PostMapping("/messages")
    public ResponseBody messageCallPricing(@RequestBody MessagePricingJSON _message) {
        ResponseBody response = new ResponseBody();
        try {
            new MessagePricing().insert(_message);
            response.getStatus().setMessage("Succés");
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | URISyntaxException | SQLException | ParseException | InvalidAmountException ex) {
            this.setError(response, ex);
            out(ex);
        }
        return response;
    }

    @GetMapping("/messages")
    public ResponseBody findMessageCallPricing() {
        ResponseBody response = new ResponseBody();
        try {
            response.getData().add(new MessagePricing().findLatest());
        } catch (IllegalAccessException | InstantiationException | NoSuchMethodException | InvocationTargetException | URISyntaxException | SQLException ex) {
            this.setError(response, ex);
            out(ex);
        }
        return response;
    }

    @GetMapping("/calls")
    public ResponseBody messagePricing(@RequestBody PricingJSON _call) {
        ResponseBody response = new ResponseBody();
        try {
            new CallPricing().insert(_call);
            response.getStatus().setMessage("Succés");
        } catch (IllegalAccessException | InvocationTargetException | URISyntaxException | SQLException | ParseException | InvalidAmountException ex) {
            this.setError(response, ex);
            out(ex);
        }
        return response;
    }

    @GetMapping("/calls/price")
    public ResponseBody findCallsPricing() {
        ResponseBody response = new ResponseBody();
        try {
            response.getData().add(new CallPricing().findLatest());
        } catch (IllegalAccessException | InstantiationException | NoSuchMethodException | InvocationTargetException | SQLException | URISyntaxException ex) {
            this.setError(response, ex);
            out(ex);
        }
        return response;
    }

    @PostMapping("/internet")
    public ResponseBody internetPricing(@RequestBody InternetPricingJSON _internet) {
        ResponseBody response = new ResponseBody();
        try {
            new InternetPricing().insert(_internet);
            response.getStatus().setMessage("Succés");
        } catch (IllegalAccessException | InvocationTargetException | URISyntaxException | SQLException | ParseException | InvalidAmountException ex) {
            this.setError(response, ex);
            out(ex);
        }
        return response;
    }

    @GetMapping("/internet")
    public ResponseBody findInternetPricing() {
        ResponseBody response = new ResponseBody();
        try {
            response.getData().add(new InternetPricing().getLastPricing());
        } catch (Exception ex) {
            this.setError(response, ex);
            out(ex);
        }
        return response;
    }

}
