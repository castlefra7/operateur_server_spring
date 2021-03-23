/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mg.operateur.business_logic.mobile_credit;

import mg.operateur.gen.FctGen;
import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import mg.operateur.business_logic.offer.Amount;
import mg.operateur.business_logic.offer.Application;
import mg.operateur.business_logic.offer.Offer;
import mg.operateur.business_logic.offer.PasswordHelper;
import mg.operateur.business_logic.offer.Purchase;
import mg.operateur.conn.ConnGen;
import mg.operateur.gen.CDate;
import mg.operateur.gen.InvalidAmountException;
import mg.operateur.gen.InvalidDateException;
import mg.operateur.gen.InvalidFormatException;
import mg.operateur.gen.NotFoundException;
import mg.operateur.gen.RequiredException;
import mg.operateur.web_services.controllers.PurchaseRepository;
import mg.operateur.web_services.resources.commons.AskJSON;
import mg.operateur.web_services.resources.consumptions.CallJSON;
import mg.operateur.web_services.resources.consumptions.InternetJSON;
import mg.operateur.web_services.resources.consumptions.MessageJSON;

/**
 *
 * @author lacha
 */
public final class Customer extends Person {

    public Customer() {
    }

    public Customer(int _id) {
        setId(_id);
    }

    private void out(Exception ex) {
        ex.printStackTrace();
        System.out.println(ex.getMessage());
    }
    // RemainingOffers:
    // Message: 10
    // Appel: 60 sec
    // Internet: 100Mo
    // Faceboobaka: 1Go

    public HashMap<String, Double> getRemainings(AskJSON _ask, PurchaseRepository purchaseRepository) throws IllegalAccessException, IllegalArgumentException, InstantiationException, NoSuchMethodException, InvocationTargetException, SQLException, NotFoundException, ParseException, InvalidAmountException {
        Connection conn = null;
        List<RemainingOffer> result = new ArrayList();
        HashMap<String, Double> remain = new HashMap();

        try {
            conn = ConnGen.getConn();
            Customer customer = this.find(_ask.getPhone_number(), conn);
            List<Purchase> validPurchases = findAllValidPurchases(customer.getId(), CDate.getDate().parse(_ask.getDate()), purchaseRepository, conn);
            // All valid purchases

            for (Purchase p : validPurchases) {
                List<Amount> amounts = p.getOffer().getAmounts();
                for (Amount amount : amounts) {
                    Character type = amount.getApplication().getT_type();
                    // TODO conver if type is Go, Mo, Mn, Hr
                    if (type == 'i') {
                        int val = new InternetPricing().convertToKo(String.valueOf(amount.getValue()).concat(amount.getApplication().getUnit().getSuffix()));
                        amount.setValue(val);
                    }

                    if (type == 'c') {
                        int val = 0;
                        if (amount.getApplication().getUnit().getSuffix().toLowerCase().equals("mn")) {
                            val = val * 60;
                        } else if (amount.getApplication().getUnit().getSuffix().toLowerCase().equals("hr")) {
                            val = val * 3600;
                        }
                        amount.setValue(val);
                    }

                    if (amount.getApplication().getInternet_application_id() == -1) {
                        Double value = remain.get(type);
                        if (value == null) {
                            remain.put(String.valueOf(type), amount.getValue());
                        } else {
                            remain.put(String.valueOf(type), value + amount.getValue());
                        }
                    } else {
                        String typeI = type + String.valueOf(amount.getApplication().getInternet_application_id());
                        Double value = remain.get(typeI);
                        if (value == null) {
                            remain.put(typeI, amount.getValue());
                        } else {
                            remain.put(typeI, value + amount.getValue());
                        }
                    }
                }

            }
        } catch (IllegalAccessException | IllegalArgumentException | InstantiationException | NoSuchMethodException | InvocationTargetException | SQLException | NotFoundException ex) {
            throw ex;
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException ex) {
                out(ex);
            }
        }

        return remain;
    }

    public void useInternet(InternetJSON _internet, PurchaseRepository purchaseRepository, Connection conn) throws ParseException, InvalidDateException, SQLException, InstantiationException, NoSuchMethodException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NotFoundException, InvalidAmountException, RequiredException {
        checkLastOperation(CDate.getDate().parse(_internet.getCreated_at()), conn);
        conn.setAutoCommit(false);
        Date date = CDate.getDate().parse(_internet.getCreated_at());
        Customer customer = this.find(_internet.getPhone_number(), conn);
        InternetPricing lastPricing = (InternetPricing) new InternetPricing().getLastPricing(date, conn);
        int amountKo = lastPricing.convertToKo(_internet.getAmount());
        int orgAmount = amountKo;

        boolean shouldUseCredit = false;

        /* use offers first */
        List<Purchase> validPurchases = findAllValidPurchases(customer.getId(), date, purchaseRepository, conn);
        if (validPurchases.size() > 0) {
            for (Purchase purchase : validPurchases) {
                List<Amount> allAmounts = purchase.getOffer().getAmounts();
                for (Amount amount : allAmounts) {
                    Application app = amount.getApplication();
                    if (app.getT_type() == 'i' && amount.getValue() > 0 && app.getInternet_application_id() == _internet.getInternet_application_id()) {
                        int val = lastPricing.convertToKo(String.valueOf(amount.getValue()).concat(app.getUnit().getSuffix()));
                        app.getUnit().setSuffix("ko");

                        double orgValue = val;
                        int remainingValue = ((int) orgValue - amountKo) > 0 ? ((int) orgValue - amountKo) : 0;
                        amount.setValue(remainingValue);
                        if (((int) orgValue - amountKo) >= 0) {
                            amountKo = 0;
                            break;
                        } else {
                            amountKo = Math.abs(((int) orgValue - amountKo));
                        }
                    }
                }
                purchaseRepository.save(purchase);
            }
        }

        if (amountKo > 0 || validPurchases.isEmpty()) {
            shouldUseCredit = true;
        }

        try {

            new InternetApplication().find(_internet.getInternet_application_id(), conn);

            if (shouldUseCredit) {
                double creditBalance = customer.creditBalance(date, conn);
                int howManyUnit = (int) Math.ceil(creditBalance / lastPricing.getAmount());
                int nUnitICanAfford = Math.min(amountKo, howManyUnit);
                if (nUnitICanAfford <= 0) {
                    throw new InvalidAmountException(String.format("Votre crédit est insuffisant"));
                }
                double priceToPay = (double) nUnitICanAfford * lastPricing.getAmount();
                CreditConsumption creditCons = new CreditConsumption();
                creditCons.setCreated_at(date);
                creditCons.setCons_amount(priceToPay);
                creditCons.setCustomer_id(customer.getId());
                creditCons.insert(conn);
                orgAmount = nUnitICanAfford; // TODO why not orgAmount - amountKo + nUnitICanAfford
            }

            InternetConsumption internetCons = new InternetConsumption();
            internetCons.setAmount(orgAmount);
            internetCons.setCreated_at(date);
            internetCons.setCustomer_id(customer.getId());
            internetCons.setInternet_application_id(_internet.getInternet_application_id());
            internetCons.insert(conn);

            conn.commit();
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | SQLException ex) {
            conn.rollback();
            throw ex;
        } finally {

        }

    }

    public void makeCall(CallJSON _call, PurchaseRepository purchaseRepository, Connection conn) throws SQLException, InstantiationException, NoSuchMethodException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NotFoundException, ParseException, InvalidAmountException, InvalidDateException, InvalidFormatException, Exception {
        checkLastOperation(CDate.getDate().parse(_call.getDate()), conn);

        Customer source = this.find(_call.getPhone_number_source(), conn);
        Customer dest = this.find(_call.getPhone_number_destination(), conn);

        if (source == dest) {
            throw new InvalidFormatException("Vérifier le numéro de destination");
        }

        Date date = CDate.getDate().parse(_call.getDate());
        CallPricing lastPricing = (CallPricing) new CallPricing().getLastPricing(date, conn);
        int numberSecond = CDate.numberSeconds(_call.getDuration());
        int orgLengthUnit = numberSecond;

        if (numberSecond <= 0) {
            throw new InvalidAmountException("Veuillez entrer une durée d'appel valide");
        }

        boolean shouldUseCredit = false;

        /**
         * ** Begin use call using offers ****
         */
        List<Purchase> validPurchases = findAllValidPurchases(source.getId(), date, purchaseRepository, conn);
        if (validPurchases.size() > 0) {
            for (Purchase purchase : validPurchases) {
                List<Amount> allAmounts = purchase.getOffer().getAmounts();
                for (Amount amount : allAmounts) {
                    Application app = amount.getApplication();
                    if (app.getT_type() == 'c' && amount.getValue() > 0) {
                        double val = amount.getValue();
                        if (amount.getApplication().getUnit().getSuffix().toLowerCase().equals("mn")) {
                            val = val * 60;
                        } else if (amount.getApplication().getUnit().getSuffix().toLowerCase().equals("hr")) {
                            val = val * 3600;
                        }
                        amount.getApplication().getUnit().setSuffix("sec");

                        double orgValue = val;
                        int remainingValue = ((int) orgValue - numberSecond) > 0 ? ((int) orgValue - numberSecond) : 0;
                        amount.setValue(remainingValue);
                        if (((int) orgValue - numberSecond) >= 0) {
                            numberSecond = 0;
                            break;
                        } else {
                            numberSecond = Math.abs(((int) orgValue - numberSecond));
                        }
                    }
                }
                purchaseRepository.save(purchase);
            }
        }

        if (numberSecond > 0 || validPurchases.isEmpty()) {
            shouldUseCredit = true;
        }

        if (shouldUseCredit) {
            // TODO CHECK IF EXTERIOR
            double creditBalance = source.creditBalance(date, conn);
            int nUnitICanAfford = Math.min((int) Math.floor(creditBalance / lastPricing.getAmount_interior()), numberSecond);
            if (nUnitICanAfford <= 0) {
                throw new InvalidAmountException("Votre crédit est insuffisant");
            }
            double priceToPay = (double) nUnitICanAfford * lastPricing.getAmount_interior();
            this.insertMessageOrCallConsumption(true, false, nUnitICanAfford, date, source.getId(), dest.getId(), priceToPay, conn);
        } else {
            this.insertMessageOrCallConsumption(false, false, orgLengthUnit, date, source.getId(), dest.getId(), 0.0, conn);
        }
    }

    public void sendMessage(MessageJSON _message, PurchaseRepository purchaseRepository, Connection conn) throws SQLException, InstantiationException, NoSuchMethodException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NotFoundException, ParseException, InvalidAmountException, InvalidDateException {
        checkLastOperation(CDate.getDate().parse(_message.getDate()), conn);

        Customer source = this.find(_message.getPhone_number_source(), conn);
        Customer dest = this.find(_message.getPhone_number_destination(), conn); // TODO create external customer

        Date date = CDate.getDate().parse(_message.getDate());
        int lengthMessage = _message.getText().length();
        MessagePricing lastPricing = (MessagePricing) new MessagePricing().getLastPricing(date, conn);
        int lengthUnit = (int) Math.ceil((double) lengthMessage / (double) lastPricing.getUnit());
        int orgLengthUnit = lengthUnit;
        if (lengthUnit <= 0) {
            throw new InvalidAmountException("Veuillez entrer un message valide");
        }
        boolean shouldUseCredit = false;

        /**
         * ** Begin Send message message using offers ****
         */
        List<Purchase> validPurchases = findAllValidPurchases(source.getId(), date, purchaseRepository, conn);
        if (validPurchases.size() > 0) {
            for (Purchase purchase : validPurchases) {
                List<Amount> allAmounts = purchase.getOffer().getAmounts();
                for (Amount amount : allAmounts) {
                    Application app = amount.getApplication();
                    if (app.getT_type() == 'm' && amount.getValue() > 0) {
                        double orgValue = amount.getValue();
                        int remainingValue = ((int) orgValue - lengthUnit) > 0 ? ((int) orgValue - lengthUnit) : 0;
                        amount.setValue(remainingValue);
                        if (((int) orgValue - lengthUnit) >= 0) {
                            lengthUnit = 0;
                            break;
                        } else {
                            lengthUnit = Math.abs(((int) orgValue - lengthUnit));
                        }

                    }
                }
                purchaseRepository.save(purchase);
            }
        }

        if (lengthUnit > 0 || validPurchases.isEmpty()) {
            shouldUseCredit = true;
        }

        if (shouldUseCredit) {
            // TODO CHECK IF EXTERIOR
            double creditBalance = source.creditBalance(date, conn);
            int howManyUnit = (int) Math.ceil(creditBalance / lastPricing.getAmount_interior());
            int nUnitICanAfford = Math.min(howManyUnit, lengthUnit);
            if (nUnitICanAfford <= 0) {
                throw new InvalidAmountException(String.format("Votre crédit est insuffisant. %d messages n'a pas été envoyé", lengthUnit));
            }
            double priceToPay = (double) nUnitICanAfford * lastPricing.getAmount_interior();
            System.out.println(priceToPay);
            this.insertMessageOrCallConsumption(shouldUseCredit, true, nUnitICanAfford, date, source.getId(), dest.getId(), priceToPay, conn);
        } else {
            this.insertMessageOrCallConsumption(false, true, orgLengthUnit, date, source.getId(), dest.getId(), 0.0, conn);
        }
    }

    public List<Purchase> findAllValidPurchases(int customer_id, Date _date, PurchaseRepository purchaseRepository, Connection conn) {
        List<Purchase> result = new ArrayList();
        List<Purchase> allPurchases = Purchase.findByCustomerId(1, purchaseRepository);
        for (Purchase purchase : allPurchases) {
            Offer offer = purchase.getOffer();
            Date endValidity = CDate.addDay(purchase.getDate(), offer.getValidityDay());
            if (endValidity.after(_date)) {
                result.add(purchase);
            }
        }
        Collections.sort(result);

        return result;
    }

    private void insertMessageOrCallConsumption(boolean useCredit, boolean isMessage, int messUnit, Date date, int custSrcId, int custDestId, double creditConsAmount, Connection conn) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, SQLException, InvalidDateException, InvalidAmountException {
        conn.setAutoCommit(false);
        try {
            MessageCallConsumption consumption = new MessageCallConsumption();
            if (isMessage) {
                consumption.setT_type('m');
            } else {
                consumption.setT_type('c');
            }
            consumption.setAmount(messUnit);
            consumption.setCreated_at(date);
            consumption.setCustomer_id(custSrcId);
            consumption.setCustomer_destination_id(custDestId);
            consumption.insert(conn);

            if (useCredit) {
                CreditConsumption creditCons = new CreditConsumption();
                creditCons.setCreated_at(date);
                creditCons.setCons_amount(creditConsAmount);
                creditCons.setCustomer_id(custSrcId);
                creditCons.insert(conn);
            }

            conn.commit();
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | SQLException ex) {
            conn.rollback();
            throw ex;
        } finally {

        }
    }

    public double mobileBalance(AskJSON _ask, Connection conn) throws SQLException, InstantiationException, NoSuchMethodException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NotFoundException, ParseException {
        double result = 0;
        Customer source = this.find(_ask.getPhone_number(), conn);
        result = source.mobileBalance(CDate.getDate().parse(_ask.getDate()), conn);
        return result;
    }

    public double mobileBalance(Date date, Connection conn) throws SQLException {
        return FctGen.getAmount(String.format("select balance from mg.customers_balances where id = %d", getId()), "balance", conn);
    }

    public double creditBalance(AskJSON _ask, Connection conn) throws SQLException, InstantiationException, NoSuchMethodException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NotFoundException, ParseException {
        double result = 0;
        Customer source = this.find(_ask.getPhone_number(), conn);
        result = source.creditBalance(CDate.getDate().parse(_ask.getDate()), conn);;
        return result;
    }

    public double creditBalance(Date date, Connection conn) throws SQLException {
        return FctGen.getAmount(String.format("select balance from mg.customers_credit_balances where id = %d", getId()), "balance", conn);
    }

    public void transferCredit(double amount, String pwd, Date date, int _customer_dest_id, Connection conn) throws SQLException, InstantiationException, NoSuchMethodException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, Exception {
        checkLastOperation(date, conn);
        conn.setAutoCommit(false);
        Customer currCust = find(getId(), conn);
        if (!currCust.getPassword().equals(PasswordHelper.md5(pwd))) {
            throw new Exception("Mot de passe incorrect");
        }

        if (amount > this.creditBalance(date, conn)) {
            throw new Exception("Votre crédit est insuffisant");
        }

        Credit credit = new Credit();
        credit.setAmount(amount);
        credit.setCustomer_id(_customer_dest_id);
        credit.setCustomer_source_id(getId());
        credit.setCreated_at(date);

        CreditConsumption cons = new CreditConsumption();
        cons.setCreated_at(date);
        cons.setCustomer_id(getId());
        cons.setCons_amount(amount);

        try {
            Customer destCustomer = new Customer(_customer_dest_id);
            destCustomer.buyCredit(credit, conn);
            cons.insert(conn);

            conn.commit();
        } catch (Exception ex) {
            conn.rollback();
            throw ex;
        } finally {

        }
    }

    public void buyCreditFromMobile(Credit credit, String pwd, Connection conn) throws SQLException, InvalidDateException, InvalidAmountException, Exception {
        checkLastOperation(credit.getCreated_at(), conn);
        conn.setAutoCommit(false);
        if (credit.getAmount() > this.mobileBalance(credit.getCreated_at(), conn)) {
            throw new InvalidAmountException("Votre solde est insuffisant");
        }
        try {
            Withdraw _withdraw = new Withdraw();
            _withdraw.setCreated_at(credit.getCreated_at());
            _withdraw.setCustomer_id(credit.getCustomer_id());
            _withdraw.setFee(0);
            _withdraw.setAmount(credit.getAmount());

            this.withdraw(_withdraw, pwd, true, conn);
            this.buyCredit(credit, conn);

            conn.commit();
        } catch (Exception ex) {
            conn.rollback();
            throw ex;
        } finally {

        }

    }

    public void buyCredit(Credit credit, Connection conn) throws SQLException, Exception {
        checkLastOperation(credit.getCreated_at(), conn);

        conn.setAutoCommit(false);

        try {
            credit.insert(conn);
            conn.commit();
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | SQLException ex) {
            conn.rollback();
            throw ex;
        } finally {

        }
    }

    public void transfer(double amount, String pwd, Date date, int _customer_dest_id, Connection conn) throws SQLException, InvocationTargetException, IllegalArgumentException, IllegalAccessException, Exception {
        checkLastOperation(date, conn);
        conn.setAutoCommit(false);
        Withdraw _withdraw = new Withdraw();
        _withdraw.setCreated_at(date);
        _withdraw.setCustomer_id(getId());
        _withdraw.setAmount(amount);

        Deposit _deposit = new Deposit();
        _deposit.setCreated_at(date);
        _deposit.setCustomer_id(_customer_dest_id);
        _deposit.setCustomer_source_id(getId());
        _deposit.setIsValidated(true);
        double amountToDeposit = _withdraw.getAmount() - getFeeAmount(amount, conn);
        _deposit.setAmount(amountToDeposit);

        try {
            withdraw(_withdraw, pwd, true, conn);
            Customer newCust = new Customer(_customer_dest_id);
            newCust.deposit(_deposit, conn);

            conn.commit();
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | SQLException ex) {
            conn.rollback();
            throw ex;
        } finally {

        }
    }

    public void withdraw(Withdraw withdraw, String pwd, boolean isFree, Connection conn) throws SQLException, InstantiationException, NoSuchMethodException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, Exception {
        checkLastOperation(withdraw.getCreated_at(), conn);

        conn.setAutoCommit(false);
        Customer currCust = find(getId(), conn);
        if (!currCust.getPassword().equals(PasswordHelper.md5(pwd))) {
            throw new Exception("Mot de passe incorrect");
        }

        if (isFree == false) {
            withdraw.setFee(getFeeAmount(withdraw.getAmount(), conn));
        }
        if ((withdraw.getAmount()) > mobileBalance(withdraw.getCreated_at(), conn)) {
            throw new Exception("Votre solde est insuffisant pour effectuer cette opération");
        }

        try {
            withdraw.insert(conn);
            conn.commit();
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | SQLException ex) {
            conn.rollback();
            throw ex;
        } finally {

        }
    }

    private void checkLastOperation(Date date, Connection conn) throws InvalidDateException, SQLException {
        Date lastOp = lastOperationDate(conn);
        if (lastOp != null) {
            if (date.compareTo(lastOp) < 0) {
                throw new InvalidDateException();
            }
        }
    }

    private double getFeeAmount(double amount, Connection conn) throws SQLException, IllegalAccessException, IllegalArgumentException, InstantiationException, InvocationTargetException, NoSuchMethodException {
        double result = 0;
        List<Object> allFees = new Fee().getAllFees(null, conn);
        for (Object ob : allFees) {
            Fee fee = (Fee) ob;
            if (amount >= fee.getAmount_min() && amount <= fee.getAmount_max()) {
                result = fee.getAmount_fee();
                break;
            }
        }
        return result;
    }

    private Date lastOperationDate(Connection conn) throws SQLException {
        // TODO: also get Purchases operation date in mongodb
        String req = String.format("select max(created_at) as max  from mg.all_customer_operations where customer_id= %d", getId());
        return FctGen.getDate(req, "max", conn);
    }

    public void deposit(Deposit deposit, Connection conn) throws SQLException, IllegalAccessException, InvocationTargetException, Exception {
        conn.setAutoCommit(false);
        this.checkLastOperation(deposit.getCreated_at(), conn);
        try {
            deposit.insert(conn);
            conn.commit();
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | SQLException ex) {
            conn.rollback();
            throw ex;
        } finally {

        }

    }

    public Customer find(String _phone_number, Connection conn) throws SQLException, InstantiationException, NoSuchMethodException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NotFoundException {
        Customer result = null;
        Object ob = FctGen.find(new Customer(), String.format("select * from %s where phone_number like '%s'", tableName(), _phone_number), columnsWithID(), conn);
        if (ob != null) {
            result = (Customer) ob;
        } else {
            throw new NotFoundException("Ce numéro n'existe pas");
        }
        return result;
    }

    public Customer find(int _id, Connection conn) throws SQLException, InstantiationException, NoSuchMethodException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        Customer result = null;
        Object ob = FctGen.find(new Customer(), String.format("select * from %s where id = %d", tableName(), _id), columnsWithID(), conn);
        if (ob != null) {
            result = (Customer) ob;
        }
        return result;
    }

    public List<Object> findAll(Connection conn) throws SQLException, NoSuchMethodException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        List<Object> result = new ArrayList();

        result = FctGen.findAll(new Customer(), String.format("select * from %s", tableName()), columnsWithID(), conn);
        return result;
    }

    /* properties */
    private String password;
    private String phone_number;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public void insert(Connection conn) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, SQLException {
        FctGen.insert(this, columns(), tableName(), conn);
    }

    @Override
    public String tableName() {
        return "mg.customers";
    }

    @Override
    public String[] columns() {
        return new String[]{"created_at", "name", "email", "phone_number", "password"};
    }

    public String[] columnsWithID() {
        return new String[]{"id", "created_at", "name", "email", "phone_number", "password"};
    }

}
