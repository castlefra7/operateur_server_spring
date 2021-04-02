/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mg.operateur.business_logic.mobile_credit;

import java.io.IOException;
import mg.operateur.gen.FctGen;
import java.lang.reflect.InvocationTargetException;
import java.net.URISyntaxException;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import mg.operateur.business_logic.offer.Amount;
import mg.operateur.business_logic.offer.Application;
import mg.operateur.business_logic.offer.PasswordHelper;
import mg.operateur.business_logic.offer.Purchase;
import mg.operateur.conn.ConnGen;
import mg.operateur.gen.CDate;
import mg.operateur.gen.InvalidAmountException;
import mg.operateur.gen.InvalidDateException;
import mg.operateur.gen.InvalidFormatException;
import mg.operateur.gen.NotFoundException;
import mg.operateur.gen.RequiredException;
import mg.operateur.web_services.controllers.MessageRepository;
import mg.operateur.web_services.controllers.PurchaseRepository;
import mg.operateur.web_services.resources.commons.AskJSON;
import mg.operateur.web_services.resources.commons.TransferJSON;
import mg.operateur.web_services.resources.consumptions.CallJSON;
import mg.operateur.web_services.resources.consumptions.InternetJSON;
import mg.operateur.web_services.resources.consumptions.MessageJSON;
import mg.operateur.web_services.resources.credit.CreditJSON;
import mg.operateur.web_services.resources.credit.CreditMobileJSON;
import mg.operateur.web_services.resources.mobilemoney.DepositJSON;
import mg.operateur.web_services.resources.mobilemoney.WithdrawJSON;

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
    public HashMap<String, Date> getOffersRemains(AskJSON _ask, PurchaseRepository purchaseRepository) throws ParseException, IllegalAccessException,
            SQLException, NotFoundException, InvalidFormatException, URISyntaxException, IllegalArgumentException, InstantiationException, NoSuchMethodException, InvocationTargetException {
        Connection conn = null;
        HashMap<String, Date> result = new HashMap();

        try {
            conn = ConnGen.getConn();
            Customer customer = this.findTrue(_ask.getPhone_number(), conn);
            List<Purchase> validPurchases = findAllValidPurchases(customer.getId(), CDate.getDate().parse(_ask.getDate()), purchaseRepository, conn);

            for (Purchase p : validPurchases) {
                Date curr = result.get(p.getOffer().getName());
                if (curr == null) {
                    result.put(p.getOffer().getName(), p.getEndDate());
                }

            }
        } catch (ParseException | IllegalAccessException | InvalidFormatException
                | URISyntaxException | IllegalArgumentException | InstantiationException
                | NoSuchMethodException | InvocationTargetException | SQLException
                | NotFoundException ex) {
            throw ex;
        } catch (IOException ex) {
            Logger.getLogger(Customer.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException ex) {
                out(ex);
            }
        }
        return result;
    }

    public HashMap<String, Double> getRemainings(AskJSON _ask, PurchaseRepository purchaseRepository) throws IllegalAccessException, IllegalArgumentException, InstantiationException, NoSuchMethodException, InvocationTargetException, SQLException, NotFoundException, ParseException, InvalidAmountException, InvalidFormatException, URISyntaxException {
        Connection conn = null;
        HashMap<String, Double> remain = new HashMap();

        try {
            conn = ConnGen.getConn();
            Customer customer = this.findTrue(_ask.getPhone_number(), conn);
            List<Purchase> validPurchases = findAllValidPurchases(customer.getId(), CDate.getDate().parse(_ask.getDate()), purchaseRepository, conn);

            for (Purchase p : validPurchases) {

                List<Amount> amounts = p.getOffer().getAmounts();
                for (Amount amount : amounts) {
                    Character type = amount.getApplication().getT_type();
                    if (type == 'i') {
                        int val = new InternetPricing().convertToKo(String.valueOf(amount.getValue()).concat(amount.getApplication().getUnit().getSuffix()));
                        amount.setValue(val);
                    }
                    String strType = String.valueOf(type);
                    if (type == 'c') {
                        int val = (int) amount.getValue();
                        if (amount.getUtilization() == null) {
                            if (amount.getApplication().getUnit().getSuffix().toLowerCase().equals("mn")) {
                                val = val * 60;
                            } else if (amount.getApplication().getUnit().getSuffix().toLowerCase().equals("hr")) {
                                val = val * 3600;
                            }
                            strType += "Sec";
                        } else {
                            strType += "Ar";
                        }
                        amount.setValue(val);
                    }

                    if (type == 'm') {
                        if (amount.getUtilization() == null) {
                            strType += "N";
                        } else {
                            strType += "Ar";
                        }
                    }

                    if (amount.getApplication().getInternet_application_id() == -1 || type == 'c' || type == 'm') {
                        Double value = remain.get(strType);
                        double res = value != null ? value : 0;
                        remain.put(strType, res + amount.getValue());
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
        } catch (IOException ex) {
            Logger.getLogger(Customer.class.getName()).log(Level.SEVERE, null, ex);
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

    public void useInternet(InternetJSON _internet, PurchaseRepository purchaseRepository, Connection conn) throws ParseException, InvalidDateException, SQLException, InstantiationException, NoSuchMethodException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NotFoundException, InvalidAmountException, RequiredException, InvalidFormatException, IOException, URISyntaxException {
        conn.setAutoCommit(false);
        Date date = CDate.getDate().parse(_internet.getCreated_at());

        Customer customer = this.findTrue(_internet.getPhone_number(), conn);
        customer.checkLastOperation(CDate.getDate().parse(_internet.getCreated_at()), conn);

        InternetPricing lastPricing = (InternetPricing) new InternetPricing().getLastPricing(conn);
        int amountKo = lastPricing.convertToKo(_internet.getAmount());
        int orgAmount = amountKo;

        boolean shouldUseCredit = false;

        /* use offers first */
        Calendar cal = new GregorianCalendar();
        cal.setTime(date);
        int currHour = cal.get(Calendar.HOUR_OF_DAY);

        boolean unlimited = false;
        List<Purchase> validPurchases = findAllValidPurchases(customer.getId(), date, purchaseRepository, conn);
        if (validPurchases.size() > 0) {
            for (Purchase purchase : validPurchases) {
                List<Amount> allAmounts = purchase.getOffer().getAmounts();
                boolean canBeUsed = purchase.getOffer().canBeUsed(currHour);
                if (canBeUsed) {
                    for (Amount amount : allAmounts) {
                        if (unlimited) {
                            break;
                        }

                        Application app = amount.getApplication();
                        if (app.getT_type() == 'i' && (amount.getValue() == 0 || amount.getValue() > 0) && app.getInternet_application_id() == _internet.getInternet_application_id()) {
                            unlimited = amount.getIsUnlimited();
                        }
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
        }

        if (amountKo > 0 || validPurchases.isEmpty()) {
            shouldUseCredit = true;
        }

        if (unlimited) {
            shouldUseCredit = false;
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

        Customer source = this.findTrue(_call.getPhone_number_source(), conn);
        Customer dest = this.find(_call.getPhone_number_destination(), conn);

        source.checkLastOperation(CDate.getDate().parse(_call.getDate()), conn);

        if (source.getId() == dest.getId()) {
            throw new InvalidFormatException("Vérifier le numéro de destination");
        }

        ConfOperator conf = new ConfOperator().getLastConf(conn);
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
        Calendar cal = new GregorianCalendar();
        cal.setTime(date);
        int currHour = cal.get(Calendar.HOUR_OF_DAY);

        boolean unlimited = false;

        List<Purchase> validPurchases = findAllValidPurchases(source.getId(), date, purchaseRepository, conn);
        if (validPurchases.size() > 0) {
            for (Purchase purchase : validPurchases) {
                List<Amount> allAmounts = purchase.getOffer().getAmounts();
                if (numberSecond <= 0) {
                    break;
                }
                if (unlimited) {
                    break;
                }
                boolean canBeUsed = purchase.getOffer().canBeUsed(currHour);
                if (canBeUsed) {
                    for (Amount amount : allAmounts) {
                        Application app = amount.getApplication();
                        if (numberSecond <= 0) {
                            break;
                        }
                        if (app.getT_type() == 'c' && (amount.getValue() == 0 || amount.getValue() > 0)) {
                            unlimited = amount.getIsUnlimited();
                        }
                        if (app.getT_type() == 'c' && amount.getValue() > 0) {

                            double val = amount.getValue();

                            if (amount.getUtilization() != null) {
                                if (amount.getUtilization().getIntra() != null && amount.getUtilization().getExtra() != null) {
                                    double price = amount.getUtilization().getIntra().getPrice();
                                    if (!PhoneNumber.getPrefix(_call.getPhone_number_destination()).equals(conf.getPrefix())) {
                                        price = amount.getUtilization().getExtra().getPrice();
                                    }
                                    double amountIShouldConsume = price * (double) numberSecond;
                                    double remainingValue = (val - amountIShouldConsume) > 0 ? (val - amountIShouldConsume) : 0;
                                    amount.setValue(remainingValue);
                                    if ((val - amountIShouldConsume) >= 0) {
                                        numberSecond = 0;
                                        break;
                                    } else {
                                        int nSecsConsumed = (int) Math.ceil(val / price);
                                        numberSecond -= nSecsConsumed;
                                    }
                                }
                            } else {
                                if (amount.getApplication().getUnit().getSuffix().toLowerCase().equals("mn")) {
                                    val = val * 60;
                                } else if (amount.getApplication().getUnit().getSuffix().toLowerCase().equals("hr")) {
                                    val = val * 3600;
                                }
                                double orgValue = val;
                                amount.getApplication().getUnit().setSuffix("sec");

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
                    }
                    purchaseRepository.save(purchase);
                }

            }
        }

        if (numberSecond > 0 || validPurchases.isEmpty()) {
            shouldUseCredit = true;
        }
        if (unlimited) {
            shouldUseCredit = false;
        }
        if (shouldUseCredit) {
            double amount = lastPricing.getAmount_interior();
            if (PhoneNumber.getPrefix(_call.getPhone_number_destination()).equals(conf.getPrefix()) == false) {
                amount = lastPricing.getAmount_exterior();
            }

            double creditBalance = source.creditBalance(date, conn);
            int nUnitICanAfford = Math.min((int) Math.floor(creditBalance / amount), numberSecond);
            if (nUnitICanAfford <= 0) {
                throw new InvalidAmountException("Votre crédit est insuffisant");
            }
            double priceToPay = (double) nUnitICanAfford * amount;
            this.insertMessageOrCallConsumption(true, false, nUnitICanAfford, date, source.getId(), dest.getId(), priceToPay, conn);
        } else {
            this.insertMessageOrCallConsumption(false, false, orgLengthUnit, date, source.getId(), dest.getId(), 0.0, conn);
        }
    }

    public void sendMessage(MessageJSON _message, PurchaseRepository purchaseRepository, MessageRepository messageRepo, Connection conn) throws SQLException, InstantiationException, NoSuchMethodException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NotFoundException, ParseException, InvalidAmountException, InvalidDateException, InvalidFormatException, IOException, URISyntaxException {

        Customer source = this.findTrue(_message.getPhone_number_source(), conn);
        Customer dest = this.find(_message.getPhone_number_destination(), conn); // TODO create external customer
        source.checkLastOperation(CDate.getDate().parse(_message.getDate()), conn);

        Date date = CDate.getDate().parse(_message.getDate());
        int lengthMessage = _message.getText().length();
        MessagePricing lastPricing = (MessagePricing) new MessagePricing().getLastPricing(date, conn);
        int lengthUnit = (int) Math.ceil((double) lengthMessage / (double) lastPricing.getUnit());
        int orgLengthUnit = lengthUnit;
        if (lengthUnit <= 0) {
            throw new InvalidAmountException("Veuillez entrer un message valide");
        }
        ConfOperator conf = new ConfOperator().getLastConf(conn);
        boolean shouldUseCredit = false;

        /**
         * ** Begin Send message message using offers ****
         */
        Calendar cal = new GregorianCalendar();
        cal.setTime(date);
        int currHour = cal.get(Calendar.HOUR_OF_DAY);
        boolean unlimited = false;

        List<Purchase> validPurchases = findAllValidPurchases(source.getId(), date, purchaseRepository, conn);
        if (validPurchases.size() > 0) {
            for (Purchase purchase : validPurchases) {
                List<Amount> allAmounts = purchase.getOffer().getAmounts();
                boolean canBeUsed = purchase.getOffer().canBeUsed(currHour);
                if (unlimited) {
                    break;
                }

                if (canBeUsed) {
                    for (Amount amount : allAmounts) {
                        Application app = amount.getApplication();
                        if (app.getT_type() == 'm' && (amount.getValue() == 0 || amount.getValue() > 0)) {
                            unlimited = amount.getIsUnlimited();
                        }

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
        }

        if (lengthUnit > 0 || validPurchases.isEmpty()) {
            shouldUseCredit = true;
        }

        if (unlimited) {
            shouldUseCredit = false;
        }

        System.out.println(lengthUnit);

        if (shouldUseCredit) {
            double amount = lastPricing.getAmount_interior();
            if (PhoneNumber.getPrefix(_message.getPhone_number_destination()).equals(conf.getPrefix()) == false) {
                amount = lastPricing.getAmount_exterior();
            }

            double creditBalance = source.creditBalance(date, conn);
            int howManyUnit = (int) Math.ceil(creditBalance / amount);
            int nUnitICanAfford = Math.min(howManyUnit, lengthUnit);
            if (nUnitICanAfford <= 0) {
                throw new InvalidAmountException(String.format("Votre crédit est insuffisant. %d messages n'a pas été envoyé", lengthUnit));
            }
            double priceToPay = (double) nUnitICanAfford * amount;
            System.out.println(priceToPay);
            this.insertMessageOrCallConsumption(shouldUseCredit, true, nUnitICanAfford, date, source.getId(), dest.getId(), priceToPay, conn);
        } else {
            this.insertMessageOrCallConsumption(false, true, orgLengthUnit, date, source.getId(), dest.getId(), 0.0, conn);
        }

        // Insert message into MongoDB
        MessageMongo message = new MessageMongo();
        message.setText(_message.getText());
        message.setCustomer_source(source.getId());
        message.setCustomer_dest(dest.getId());
        message.setDate(date);
        messageRepo.save(message);
    }

    public List<Purchase> findAllValidPurchases(int customer_id, Date _date, PurchaseRepository purchaseRepository, Connection conn) {

        List<Purchase> result = purchaseRepository.findByEndDateGreaterThanAndCustomer_id(_date, customer_id);
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

    public double mobileBalance(AskJSON _ask) throws SQLException, InstantiationException, NoSuchMethodException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NotFoundException, ParseException, InvalidFormatException, URISyntaxException, IOException {
        Connection conn = null;
        double result = 0;
        try {
            conn = ConnGen.getConn();
            result = mobileBalance(_ask, conn);
        } catch (IllegalAccessException | IllegalArgumentException | InstantiationException | NoSuchMethodException | InvocationTargetException | URISyntaxException | SQLException | ParseException | InvalidFormatException | NotFoundException ex) {
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
        return result;
    }

    public double mobileBalance(AskJSON _ask, Connection conn) throws SQLException, InstantiationException, NoSuchMethodException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NotFoundException, ParseException, InvalidFormatException, IOException, URISyntaxException {
        double result = 0;
        Customer source = this.findTrue(_ask.getPhone_number());
        result = source.mobileBalance(CDate.getDate().parse(_ask.getDate()), conn);
        return result;
    }

    public double mobileBalance(Date date, Connection conn) throws SQLException {
        return FctGen.getAmount(String.format("select balance from mg.customers_balances where id = %d", getId()), "balance", conn);
    }

    public double creditBalance(AskJSON _ask) throws SQLException, InstantiationException, NoSuchMethodException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NotFoundException, ParseException, InvalidFormatException, URISyntaxException, IOException {
        Connection conn = null;
        double balance = 0;
        try {
            conn = ConnGen.getConn();
            balance = new Customer().creditBalance(_ask, conn);
        } catch (IllegalAccessException | IllegalArgumentException | InstantiationException | NoSuchMethodException | InvocationTargetException | URISyntaxException | SQLException | ParseException | InvalidFormatException | NotFoundException ex) {
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
        return balance;
    }

    public double creditBalance(AskJSON _ask, Connection conn) throws SQLException, InstantiationException, NoSuchMethodException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NotFoundException, ParseException, InvalidFormatException, IOException, URISyntaxException {
        double result = 0;
        Customer source = this.findTrue(_ask.getPhone_number());
        result = source.creditBalance(CDate.getDate().parse(_ask.getDate()), conn);;
        return result;
    }

    public double creditBalance(Date date, Connection conn) throws SQLException {
        return FctGen.getAmount(String.format("select balance from mg.customers_credit_balances where id = %d", getId()), "balance", conn);
    }

    public void transferCredit(TransferJSON _transfer) throws SQLException, InstantiationException, NoSuchMethodException,
            IllegalAccessException, IllegalArgumentException, InvocationTargetException, InvalidDateException, NoSuchAlgorithmException, RequiredException, URISyntaxException, ParseException, InvalidFormatException, NotFoundException, InvalidAmountException, IOException {

        Connection conn = null;
        try {
            conn = ConnGen.getConn();
            Customer customer = new Customer().findTrue(_transfer.getPhone_number(), conn);
            Customer customerDest = new Customer().findTrue(_transfer.getPhone_number_destination(), conn);
            customer.transferCredit(_transfer.getAmount(), _transfer.getPassword(), CDate.getDate().parse(_transfer.getDate()), customerDest.getId(), conn);
        } catch (IllegalAccessException | IllegalArgumentException | InstantiationException | NoSuchMethodException | InvocationTargetException | URISyntaxException | SQLException | ParseException | InvalidFormatException | NotFoundException ex) {
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

    }

    public void transferCredit(double amount, String pwd, Date date, int _customer_dest_id, Connection conn) throws SQLException,
            InstantiationException, NoSuchMethodException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, InvalidDateException, NoSuchAlgorithmException, RequiredException, InvalidAmountException {
        checkLastOperation(date, conn);
        conn.setAutoCommit(false);
        Customer currCust = find(getId(), conn);
        if (!currCust.getPassword().equals(PasswordHelper.md5(pwd))) {
            throw new RequiredException("Mot de passe incorrect");
        }

        if (amount > this.creditBalance(date, conn)) {
            throw new InvalidAmountException("Votre crédit est insuffisant");
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
            destCustomer.insertCredit(credit, conn);
            cons.insert(conn);

            conn.commit();
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | SQLException | InvalidDateException ex) {
            conn.rollback();
            throw ex;
        } finally {

        }
    }

    public void buyCreditFromMobile(CreditMobileJSON _credit) throws SQLException, InvalidDateException, InvalidAmountException, IllegalAccessException, IllegalArgumentException, InstantiationException, NoSuchMethodException, InvocationTargetException, NoSuchAlgorithmException, NoSuchAlgorithmException, RequiredException, URISyntaxException, ParseException, InvalidFormatException, NotFoundException, IOException {

        Connection conn = null;
        try {
            conn = ConnGen.getConn();
            Customer customer = new Customer().findTrue(_credit.getPhone_number());
            Credit credit = new Credit();
            credit.setCreated_at(CDate.getDate().parse(_credit.getDate()));
            credit.setAmount(_credit.getAmount());
            credit.setCustomer_id(customer.getId());
            credit.setCustomer_source_id(customer.getId());

            customer.buyCreditFromMobile(credit, _credit.getPassword(), conn);
        } catch (IllegalAccessException | IllegalArgumentException | InstantiationException | NoSuchMethodException | InvocationTargetException | URISyntaxException | NoSuchAlgorithmException | SQLException | ParseException | InvalidAmountException | InvalidDateException | InvalidFormatException | NotFoundException | RequiredException ex) {
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
    }

    public void buyCreditFromMobile(Credit credit, String pwd, Connection conn) throws SQLException, InvalidDateException, InvalidAmountException, IllegalAccessException, IllegalArgumentException, InstantiationException, NoSuchMethodException, InvocationTargetException, NoSuchAlgorithmException, NoSuchAlgorithmException, RequiredException {
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
            this.insertCredit(credit, conn);

            conn.commit();
        } catch (IllegalAccessException | IllegalArgumentException | InstantiationException | NoSuchMethodException | InvocationTargetException | NoSuchAlgorithmException | SQLException | InvalidAmountException | InvalidDateException | RequiredException ex) {
            conn.rollback();
            throw ex;
        }

    }

    public void buyCredit(CreditJSON _credit) throws SQLException, InstantiationException,
            NoSuchMethodException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException,
            NotFoundException, InvalidFormatException,
            InvalidAmountException, ParseException, InvalidDateException, URISyntaxException, IOException {

        Connection conn = null;
        try {
            conn = ConnGen.getConn();

            Customer customer = new Customer().findTrue(_credit.getPhone_number(), conn);

            Credit credit = new Credit();
            credit.setCustomer_id(customer.getId());
            credit.setAmount(_credit.getAmount());
            credit.setCustomer_source_id(customer.getId());
            credit.setCreated_at(CDate.getDate().parse(_credit.getDate()));
            customer.insertCredit(credit, conn);

        } catch (IllegalAccessException | IllegalArgumentException | InstantiationException | NoSuchMethodException | InvocationTargetException | URISyntaxException | SQLException | ParseException | InvalidAmountException | InvalidDateException | InvalidFormatException | NotFoundException ex) {
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

    }

    public void insertCredit(Credit credit, Connection conn) throws InvalidDateException, SQLException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
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

    public void transfer(TransferJSON _transfer) throws SQLException, InvocationTargetException, IllegalArgumentException, IllegalAccessException, NoSuchMethodException, InstantiationException, URISyntaxException, ParseException, InvalidAmountException, NoSuchAlgorithmException, InvalidDateException, InvalidFormatException, NotFoundException, RequiredException, IOException {
        Connection conn = null;
        try {
            conn = ConnGen.getConn();
            Customer customer;
            customer = new Customer().findTrue(_transfer.getPhone_number());
            Customer customerDest = new Customer().findTrue(_transfer.getPhone_number_destination());
            String pwd = _transfer.getPassword();

            customer.transfer(_transfer.getAmount(), pwd, CDate.getDate().parse(_transfer.getDate()), customerDest.getId(), conn);
        } catch (IllegalAccessException | IllegalArgumentException | InstantiationException | NoSuchMethodException | InvocationTargetException | URISyntaxException | NoSuchAlgorithmException | SQLException | ParseException | InvalidAmountException | InvalidDateException | InvalidFormatException | NotFoundException | RequiredException ex) {
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
    }

    public void transfer(double amount, String pwd, Date date, int _customer_dest_id, Connection conn) throws SQLException, InvocationTargetException,
            IllegalArgumentException, IllegalAccessException, InvalidDateException, InvalidAmountException, InstantiationException, NoSuchMethodException, RequiredException, NoSuchAlgorithmException {
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

    public void withdraw(WithdrawJSON _withdraw) throws SQLException, InstantiationException, NoSuchMethodException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, InvalidDateException, RequiredException, InvalidAmountException, NoSuchAlgorithmException, URISyntaxException, ParseException, InvalidFormatException, InvalidFormatException, NotFoundException, IOException {
        Connection conn = null;
        try {
            conn = ConnGen.getConn();
            Customer customer;
            customer = new Customer().findTrue(_withdraw.getPhone_number(), conn);

            Withdraw withdraw = new Withdraw();
            withdraw.setCreated_at(CDate.getDate().parse(_withdraw.getDate()));

            withdraw.setAmount(_withdraw.getAmount());
            withdraw.setCustomer_id(customer.getId());
            String pwd = _withdraw.getPassword();

            customer.withdraw(withdraw, pwd, false, conn);
        } catch (IllegalAccessException | IllegalArgumentException | InstantiationException | NoSuchMethodException | InvocationTargetException | URISyntaxException | NoSuchAlgorithmException | SQLException | ParseException | InvalidAmountException | InvalidDateException | InvalidFormatException | NotFoundException | RequiredException ex) {
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

    }

    public void withdraw(Withdraw withdraw, String pwd, boolean isFree, Connection conn) throws SQLException, InstantiationException, NoSuchMethodException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, InvalidDateException, RequiredException, InvalidAmountException, NoSuchAlgorithmException {
        checkLastOperation(withdraw.getCreated_at(), conn);

        conn.setAutoCommit(false);
        Customer currCust = find(getId(), conn);

        if (pwd == null || !currCust.getPassword().equals(PasswordHelper.md5(pwd))) {
            throw new RequiredException("Mot de passe incorrect");
        }

        if (isFree == false) {
            withdraw.setFee(getFeeAmount(withdraw.getAmount(), conn));
            if ((withdraw.getAmount() + withdraw.getFee()) > mobileBalance(withdraw.getCreated_at(), conn)) {
                throw new InvalidAmountException("Votre solde est insuffisant pour effectuer cette opération");
            }
        }
        if ((withdraw.getAmount()) > mobileBalance(withdraw.getCreated_at(), conn)) {
            throw new InvalidAmountException("Votre solde est insuffisant pour effectuer cette opération");
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

    public void checkLastOperation(Date date, Connection conn) throws InvalidDateException, SQLException {
        Date lastOp = lastOperationDate(conn);
        if (lastOp != null) {
            if (date.compareTo(lastOp) < 0) {
                throw new InvalidDateException();
            }
        }
    }

    private double getFeeAmount(double amount, Connection conn) throws SQLException, IllegalAccessException, IllegalArgumentException, InstantiationException, InvocationTargetException, NoSuchMethodException {
        double result = 0;
        List<Object> allFees = new Fee().getAllFees(conn);
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
        String req = String.format("select max(created_at) as max  from mg.all_customer_operations where customer_id= %d", getId());
        return FctGen.getDate(req, "max", conn);
    }

    public void deposit(DepositJSON _deposit) throws SQLException, IllegalAccessException, InvocationTargetException, IllegalArgumentException, IllegalArgumentException, InstantiationException, InstantiationException, NoSuchMethodException, URISyntaxException, ParseException, InvalidAmountException, InvalidDateException, InvalidFormatException, NotFoundException, IOException {
        Connection conn = null;
        try {
            conn = ConnGen.getConn();
            Customer customer;
            customer = new Customer().findTrue(_deposit.getPhone_number());

            Deposit deposit = new Deposit();
            deposit.setCreated_at(CDate.getDate().parse(_deposit.getDate()));
            deposit.setCustomer_id(customer.getId());
            deposit.setCustomer_source_id(customer.getId());
            deposit.setAmount(_deposit.getAmount());

            customer.deposit(deposit, conn);
        } catch (IllegalAccessException | IllegalArgumentException | InstantiationException | NoSuchMethodException | InvocationTargetException | URISyntaxException | SQLException | ParseException | InvalidAmountException | InvalidDateException | InvalidFormatException | NotFoundException ex) {
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
    }

    public void deposit(Deposit deposit, Connection conn) throws SQLException, IllegalAccessException, InvocationTargetException, InvalidDateException {
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

    public Customer getExteriorCust(Connection conn) throws SQLException, InstantiationException, NoSuchMethodException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NotFoundException {
        return (Customer) FctGen.find(this, "select * from mg.customers where phone_number = '0'", columnsWithID(), conn);
    }

    public String phoneExists(String _phone_number) throws URISyntaxException, SQLException, IOException {
        Connection conn = null;
        String result = null;
        try {
            conn = ConnGen.getConn();
            result = phoneExists(_phone_number, conn);
        } catch (URISyntaxException | SQLException ex) {
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

        return result;
    }

    public String phoneExists(String _phone_number, Connection conn) {
        String result;
        try {
            Customer cust = this.find(_phone_number, conn);
            result = cust.getPhone_number();
        } catch (IllegalAccessException | IllegalArgumentException | InstantiationException | NoSuchMethodException | InvocationTargetException | SQLException | InvalidFormatException | NotFoundException ex) {
            result = null;
        }

        return result;
    }

    public Customer find(String _phone_number) throws SQLException, InstantiationException, NoSuchMethodException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NotFoundException, InvalidFormatException, URISyntaxException, IOException {
        Connection conn = null;
        Customer result = null;
        try {
            conn = ConnGen.getConn();
            result = find(_phone_number, conn);
        } catch (IllegalAccessException | IllegalArgumentException | InstantiationException | NoSuchMethodException | InvocationTargetException | URISyntaxException | SQLException | InvalidFormatException | NotFoundException ex) {
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
        return result;
    }

    public Customer findTrue(String _phone_number, Connection conn) throws SQLException, InstantiationException, NoSuchMethodException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NotFoundException, InvalidFormatException, IOException, URISyntaxException {

        Customer result = null;
        Object ob = FctGen.find(new Customer(), String.format("select * from %s where phone_number = '%s'", tableName(), PhoneNumber.getValidNumber(_phone_number)), columnsWithID(), conn);

        if (ob != null) {
            result = (Customer) ob;
        } else {
            throw new NotFoundException("Ce numéro n'existe pas");
        }

        return result;
    }

    public Customer findTrue(String _phone_number) throws SQLException, InstantiationException, NoSuchMethodException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NotFoundException, InvalidFormatException, IOException, URISyntaxException {
        Connection conn = null;
        Customer result = null;
        try {
            conn = ConnGen.getConn();
            Object ob = FctGen.find(new Customer(), String.format("select * from %s where phone_number = '%s'", tableName(), PhoneNumber.getValidNumber(_phone_number)), columnsWithID(), conn);

            if (ob != null) {
                result = (Customer) ob;
            } else {
                throw new NotFoundException("Ce numéro n'existe pas");
            }
        } catch (IllegalAccessException | IllegalArgumentException | InstantiationException | NoSuchMethodException | InvocationTargetException | URISyntaxException | SQLException | InvalidFormatException | NotFoundException ex) {
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
        return result;
    }

    public Customer find(String _phone_number, Connection conn) throws SQLException, InstantiationException, NoSuchMethodException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NotFoundException, InvalidFormatException {

        Customer result = null;
        ConfOperator conf = new ConfOperator().getLastConf(conn);
        if (PhoneNumber.getPrefix(_phone_number).equals(conf.getPrefix()) == false) {
            return getExteriorCust(conn);
        }

        Object ob = FctGen.find(new Customer(), String.format("select * from %s where phone_number = '%s'", tableName(), PhoneNumber.getValidNumber(_phone_number)), columnsWithID(), conn);

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
