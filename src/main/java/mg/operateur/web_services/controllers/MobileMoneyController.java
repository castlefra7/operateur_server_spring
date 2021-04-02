package mg.operateur.web_services.controllers;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URISyntaxException;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.text.ParseException;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import mg.operateur.business_logic.mobile_credit.Customer;
import mg.operateur.business_logic.mobile_credit.Fee;
import mg.operateur.business_logic.notifications.NotificationMobileMoney;
import mg.operateur.gen.InvalidAmountException;
import mg.operateur.gen.InvalidDateException;
import mg.operateur.gen.InvalidFormatException;
import mg.operateur.gen.NotFoundException;
import mg.operateur.gen.RequiredException;
import mg.operateur.web_services.ResponseBody;
import mg.operateur.web_services.resources.commons.AskJSON;
import mg.operateur.web_services.resources.commons.MessageJSON;
import mg.operateur.web_services.resources.mobilemoney.DepositJSON;
import mg.operateur.web_services.resources.commons.TransferJSON;
import mg.operateur.web_services.resources.credit.CreditMobileJSON;
import mg.operateur.web_services.resources.mobilemoney.FeeJSON;
import mg.operateur.web_services.resources.mobilemoney.WithdrawJSON;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RequestMapping("/mobilemoney")
@CrossOrigin(origins = "*")
@RestController
public class MobileMoneyController {

    private void out(Exception ex) {
        ex.printStackTrace();
        System.out.println(ex.getMessage());
    }

    private void setError(ResponseBody response, Exception ex) {
        response.getStatus().setCode(500);
        response.getStatus().setMessage(ex.getMessage());
    }

    @GetMapping("/balance")
    public ResponseBody balance(@RequestBody AskJSON _ask) throws IOException {
        ResponseBody response = new ResponseBody();
        try {
            double balance = new Customer().mobileBalance(_ask);
            response.getData().add(new MessageJSON("Solde de votre mobile money est de " + balance));
        } catch (IllegalAccessException | IllegalArgumentException | InstantiationException | NoSuchMethodException | InvocationTargetException | URISyntaxException | SQLException | ParseException | InvalidFormatException | NotFoundException ex) {
            setError(response, ex);
            out(ex);
        }
        return response;
    }

    @PostMapping("/buycredit")
    public ResponseBody buyCredit(@RequestBody CreditMobileJSON _credit) throws IOException {
        ResponseBody response = new ResponseBody();
        try {
            new Customer().buyCreditFromMobile(_credit);
            response.getStatus().setMessage("Succés");
            response.getData().add(NotificationMobileMoney.notiBuyCredit(_credit.getAmount(), _credit.getDate(), _credit.getPhone_number()));
        } catch (IllegalAccessException | IllegalArgumentException | InstantiationException | NoSuchMethodException | InvocationTargetException | URISyntaxException | NoSuchAlgorithmException | SQLException | ParseException | InvalidAmountException | InvalidDateException | InvalidFormatException | NotFoundException | RequiredException ex) {
            this.setError(response, ex);
            out(ex);
        }
        return response;
    }

    @PostMapping("/fees")
    public ResponseBody fees(@RequestBody FeeJSON _fee) throws URISyntaxException, IOException {
        ResponseBody response = new ResponseBody();
        try {
            new Fee().insert(_fee);
            response.getStatus().setMessage("Succés");
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | URISyntaxException | SQLException | ParseException | InvalidAmountException ex) {
            setError(response, ex);
            out(ex);
        }
        return response;
    }

    @RequestMapping()
    public ResponseBody index() {
        ResponseBody response = new ResponseBody();
        response.getStatus().setMessage("Mobile money");
        return response;
    }

    @PostMapping("/deposit")
    public ResponseBody deposit(@RequestBody DepositJSON _deposit) throws IOException {
        ResponseBody response = new ResponseBody();
        try {
            new Customer().deposit(_deposit);
            response.getStatus().setMessage("Succés");
            response.getData().add(NotificationMobileMoney.notifyDeposit(_deposit.getAmount(), _deposit.getDate(), _deposit.getPhone_number()));
        } catch (IllegalAccessException | IllegalArgumentException | InstantiationException | NoSuchMethodException | InvocationTargetException | URISyntaxException | SQLException | ParseException | InvalidAmountException | InvalidDateException | InvalidFormatException | NotFoundException ex) {
            setError(response, ex);
            out(ex);
        }
        return response;
    }

    @PostMapping("/withdraw")
    public ResponseBody withdraw(@RequestBody WithdrawJSON _withdraw) throws IOException {
        ResponseBody response = new ResponseBody();
        try {
            new Customer().withdraw(_withdraw);
            response.getStatus().setMessage("Succés");
            response.getData().add(NotificationMobileMoney.notifyWithdraw(_withdraw.getAmount(), _withdraw.getDate(), _withdraw.getPhone_number()));
        } catch (IllegalAccessException | IllegalArgumentException | InstantiationException | NoSuchMethodException | InvocationTargetException | URISyntaxException | NoSuchAlgorithmException | SQLException | ParseException | InvalidAmountException | InvalidDateException | InvalidFormatException | NotFoundException | RequiredException ex) {
            setError(response, ex);
            out(ex);
        }
        return response;
    }

    @PostMapping("/transfer")
    public ResponseBody transfer(@RequestBody TransferJSON _transfer) throws IOException {
        ResponseBody response = new ResponseBody();
        try {
            new Customer().transfer(_transfer);
            response.getStatus().setMessage("Succés");
            response.getData().add(NotificationMobileMoney.notifyTransfert(_transfer.getAmount(), _transfer.getDate(), _transfer.getPhone_number(), _transfer.getPhone_number_destination()));
        } catch (IllegalAccessException | IllegalArgumentException | InstantiationException | NoSuchMethodException | InvocationTargetException | URISyntaxException | NoSuchAlgorithmException | SQLException | ParseException | InvalidAmountException | InvalidDateException | InvalidFormatException | NotFoundException | RequiredException ex) {
            setError(response, ex);
            out(ex);
        }
        return response;
    }

}
