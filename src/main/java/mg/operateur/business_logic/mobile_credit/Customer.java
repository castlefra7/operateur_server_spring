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
import java.util.Date;
import java.util.List;
import mg.operateur.business_logic.offer.Amount;
import mg.operateur.business_logic.offer.Application;
import mg.operateur.business_logic.offer.Offer;
import mg.operateur.business_logic.offer.PasswordHelper;
import mg.operateur.business_logic.offer.Purchase;
import mg.operateur.gen.CDate;
import mg.operateur.gen.InvalidAmountException;
import mg.operateur.gen.InvalidDateException;
import mg.operateur.gen.InvalidFormatException;
import mg.operateur.gen.NotFoundException;
import mg.operateur.web_services.controllers.PurchaseRepository;
import mg.operateur.web_services.resources.consumptions.CallJSON;
import mg.operateur.web_services.resources.consumptions.MessageJSON;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author lacha
 */
public class Customer extends Person {
    
   
    public Customer(){}
    public Customer(int _id) {
        setId(_id);
    }
    
    public void makeCall(CallJSON _call, Connection conn) throws SQLException, InstantiationException, NoSuchMethodException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NotFoundException, ParseException, InvalidAmountException, InvalidDateException, InvalidFormatException{
        conn.setAutoCommit(false);
        Date date = CDate.getDate().parse(_call.getDate());
        Customer source = this.find(_call.getPhone_number_source(), conn);
        Customer dest = this.find(_call.getPhone_number_destination(), conn);
        CallPricing lastPricing = (CallPricing) new CallPricing().getLastPricing(date, conn);
        int numberSecond = CDate.numberSeconds(_call.getDuration());
        if(numberSecond <= 0) throw new InvalidAmountException("Veuillez entrer une durée d'appel valide");

        // TODO CHECK IF EXTERIOR
        // TODO check last operation of message, calls, internet, deposits, withdraws, transfers, buy credit, buy offer, purchase offer and So date should be >= lastDate
        double creditBalance = source.creditBalance(date, conn);
        int nUnitICanAfford = Math.min((int)Math.floor(creditBalance / lastPricing.getAmount_interior()), numberSecond);
        if(nUnitICanAfford <= 0) throw new InvalidAmountException("Votre crédit est insuffisant");
        double priceToPay = (double)nUnitICanAfford * lastPricing.getAmount_interior();
        
        try {
            MessageCallConsumption consumption = new MessageCallConsumption();
            consumption.setT_type('c');
            consumption.setAmount(nUnitICanAfford);
            consumption.setCreated_at(date);
            consumption.setCustomer_id(source.getId());
            consumption.setCustomer_destination_id(dest.getId());
            consumption.insert(conn);
            
            CreditConsumption creditCons = new CreditConsumption();
            creditCons.setCreated_at(date);
            creditCons.setCons_amount(priceToPay);
            creditCons.setCustomer_id(source.getId());
            creditCons.insert(conn);
            
            conn.commit();
        } catch(IllegalAccessException | IllegalArgumentException | InvocationTargetException | SQLException ex) {
            conn.rollback();
            throw ex;
        } finally {
            
        }
    }
    
    public boolean useMessageOffer(MessageJSON _message, Connection conn) {
        boolean result = false;
        // should have a table: customer_id, created_at, offer_id, all_amounts with remainings
        // get all offers of this where offer.buy_date + duration >= currentDate
        // order the offer by priority
        // for each offers get all amounts, 
            // if amout contains t_type == 'm' and remaining_amout > 0
                // remaining = remaing - messageLength > 0 ? remaing - messageLength: 0
                // messageconsumption.amount = min(remaing, messageLength)
                //  remaing - messageLength >= 0 break
                // else go to the next offer and apply same algorithm
                // if there is no more offer with t_type == 'm' then apply credit algorithm.
        
        return result;
    }
    
    public void sendMessage(MessageJSON _message, PurchaseRepository purchaseRepository, Connection conn) throws SQLException, InstantiationException, NoSuchMethodException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NotFoundException, ParseException, InvalidAmountException, InvalidDateException, Exception {
        Customer source = this.find(_message.getPhone_number_source(), conn);
        Customer dest = this.find(_message.getPhone_number_destination(), conn); // TODO create external customer
        
        Date date = CDate.getDate().parse(_message.getDate());
        int lengthMessage=  _message.getText().length();
        MessagePricing lastPricing = (MessagePricing) new MessagePricing().getLastPricing(date, conn);
        int lengthUnit = (int) Math.ceil((double)lengthMessage / (double)lastPricing.getUnit());
        int orgLengthUnit = lengthUnit;
        if(lengthUnit <= 0) throw new InvalidAmountException("Veuillez entrer un message valide");
        boolean shouldUseCredit = false;
        
        /**** Begin Send message message using offers *****/
        List<Purchase> validPurchases = findAllValidPurchases(source.getId(), date, purchaseRepository, conn);
        if(validPurchases.size() > 0) {
            for(Purchase purchase: validPurchases) {
                List<Amount> allAmounts = purchase.getOffer().getAmounts();
                for(Amount amount: allAmounts) {
                    Application app = amount.getApplication();
                    if(app.getT_type() == 'm' && amount.getValue() > 0) {
                        int remainingValue = ((int)amount.getValue() - lengthUnit) > 0 ? ((int)amount.getValue() - lengthUnit) : 0 ;
                        amount.setValue(remainingValue);
                        // messageconsumption.amount = min(remaing, messageLength)
                        if(((int)amount.getValue() - lengthUnit) >=  0) {
                            break;
                        }
                        lengthUnit = Math.abs(((int)amount.getValue() - lengthUnit));
                    }
                }
                purchaseRepository.save(purchase);
            }
        }
        
        
        if(lengthUnit > 0) {
            shouldUseCredit = true;
        }
        
        
        if(shouldUseCredit) {
            // TODO CHECK IF EXTERIOR
            // TODO check last operation of message, calls, internet, deposits, withdraws, transfers, buy credit, buy offer and So date should be >= lastDate
            double creditBalance = source.creditBalance(date, conn);
            int howManyUnit = (int)Math.ceil(creditBalance / lastPricing.getAmount_interior());
            int nUnitICanAfford = Math.min(howManyUnit, lengthUnit);
            if(nUnitICanAfford <= 0) throw new InvalidAmountException("Votre crédit est insuffisant");
            double priceToPay = (double)nUnitICanAfford * lastPricing.getAmount_interior();
            this.insertMessageCreditConsumption(shouldUseCredit, nUnitICanAfford, date, source.getId(), dest.getId(), priceToPay, conn);
        } else {
            this.insertMessageCreditConsumption(false, orgLengthUnit, date, source.getId(), dest.getId(), 0.0, conn);
        }
      
    }
    
    public List<Purchase> findAllValidPurchases(int customer_id, Date _date, PurchaseRepository purchaseRepository, Connection conn) throws Exception {
        List<Purchase> result = new ArrayList();
        List<Purchase> allPurchases = Purchase.findByCustomerId(1, purchaseRepository);
        // TODO sort by Offer priority
        for(Purchase purchase: allPurchases) {
            Offer offer = purchase.getOffer();
            Date endValidity = CDate.addDay(offer.getCreatedAt(), offer.getValidityDay());
            if(endValidity.after(_date)) result.add(purchase);
        }
        
        return result;
    }
    
    private void insertMessageCreditConsumption(boolean useCredit, int messUnit, Date date, int custSrcId, int custDestId, double creditConsAmount, Connection conn) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, SQLException, InvalidDateException, InvalidAmountException {
        conn.setAutoCommit(false);
        try {
            MessageCallConsumption consumption = new MessageCallConsumption();
            consumption.setT_type('m');
            consumption.setAmount(messUnit);
            consumption.setCreated_at(date);
            consumption.setCustomer_id(custSrcId);
            consumption.setCustomer_destination_id(custDestId);
            consumption.insert(conn);
            
            if(useCredit) {
                CreditConsumption creditCons = new CreditConsumption();
                creditCons.setCreated_at(date);
                creditCons.setCons_amount(creditConsAmount);
                creditCons.setCustomer_id(custSrcId);
                creditCons.insert(conn);
            }
            
            
            conn.commit();
        } catch(IllegalAccessException | IllegalArgumentException | InvocationTargetException | SQLException ex) {
            conn.rollback();
            throw ex;
        } finally {
            
        }
    }
    
    public double mobileBalance(Date date, Connection conn) throws SQLException {
        return FctGen.getAmount(String.format("select balance from mg.customers_balances where id = %d", getId()), "balance", conn);
    }
    
    public double creditBalance(Date date, Connection conn) throws SQLException {
        return FctGen.getAmount(String.format("select balance from mg.customers_credit_balances where id = %d", getId()), "balance", conn);
    }
    
    public void transferCredit(double amount, String pwd, Date date, int _customer_dest_id, Connection conn) throws SQLException, InstantiationException, NoSuchMethodException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, Exception {
        conn.setAutoCommit(false);
        Date lastOp = lastCreditOpDate(conn);
        if(lastOp!=null) if(date.compareTo(lastOp) <0) throw new Exception("Veuillez vérifier la date de l'opération");
        
        Customer currCust = find(getId(), conn);
        if(!currCust.getPassword().equals(PasswordHelper.md5(pwd))) throw new Exception("Mot de passe incorrect");
        if(amount > this.creditBalance(date, conn)) throw new Exception("Votre crédit est insuffisant");
        
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
        }  catch(Exception ex) {
            conn.rollback();
            throw ex;
        } finally {
            
        }
    }
    
    public void buyCreditFromMobile(Credit credit, String pwd, Connection conn) throws SQLException, InvalidDateException, InvalidAmountException, Exception {
        conn.setAutoCommit(false);
        
        Date lastOp = lastCreditOpDate(conn);
        if(lastOp!=null) if(credit.getCreated_at().compareTo(lastOp) <0) throw new InvalidDateException();
        if(credit.getAmount() > this.mobileBalance(credit.getCreated_at(), conn)) throw new InvalidAmountException("Votre solde est insuffisant");
       
        try {
            Withdraw _withdraw = new Withdraw();
            _withdraw.setCreated_at(credit.getCreated_at());
            _withdraw.setCustomer_id(credit.getCustomer_id());
            _withdraw.setFee(0);
            _withdraw.setAmount(credit.getAmount());
            
            this.withdraw(_withdraw, pwd, true, conn);
            this.buyCredit(credit, conn);
            
            conn.commit();
        } catch(Exception ex) {
            conn.rollback();
            throw ex;
        } finally {
            
        }
        
    }
    
    public void buyCredit(Credit credit, Connection conn) throws SQLException, Exception {
        conn.setAutoCommit(false);
        Date lastOp = lastCreditOpDate(conn);
        if(lastOp!=null) if(credit.getCreated_at().compareTo(lastOp) <0) throw new Exception("Veuillez vérifier la date de l'opération");
        
        try {
            credit.insert(conn);
            conn.commit();
        }catch(IllegalAccessException | IllegalArgumentException | InvocationTargetException | SQLException ex) {
            conn.rollback();
            throw ex;
        } finally {
            
        }
    }
    
    public void transfer(double amount, String pwd, Date date, int _customer_dest_id, Connection conn) throws SQLException, InvocationTargetException, IllegalArgumentException, IllegalAccessException, Exception{
        conn.setAutoCommit(false);
        Withdraw _withdraw = new Withdraw();
        _withdraw.setCreated_at(date);
        _withdraw.setCustomer_id(getId());
        _withdraw.setAmount(amount);
        _withdraw.setFee(getFeeAmount(amount,conn));
        
        Deposit _deposit = new Deposit();
        _deposit.setCreated_at(date);
        _deposit.setCustomer_id(_customer_dest_id);
        _deposit.setCustomer_source_id(getId());
        double amountToDeposit = _withdraw.getAmount() - _withdraw.getFee();
        _deposit.setAmount(amountToDeposit);
        
        try {
            withdraw(_withdraw, pwd, false, conn);
            Customer newCust = new Customer(_customer_dest_id);
            newCust.deposit(_deposit, conn);
            
            conn.commit();
        } catch(IllegalAccessException | IllegalArgumentException | InvocationTargetException | SQLException ex) {
            conn.rollback();
            throw ex;
        } finally {
            
        }
    }
      
    public void withdraw(Withdraw withdraw, String pwd, boolean isFree, Connection conn) throws SQLException, InstantiationException, NoSuchMethodException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, Exception {
        conn.setAutoCommit(false);
        Date lastOp = lastOperationDate(null, conn);
        if(lastOp != null)if(withdraw.getCreated_at().compareTo(lastOp) < 0) throw new Exception("Veuillez vérifier la date de l'opération");

        Customer currCust = find(getId(), conn);
        if(!currCust.getPassword().equals(PasswordHelper.md5(pwd))) throw new Exception("Mot de passe incorrect");

        if(isFree == false)withdraw.setFee(getFeeAmount(withdraw.getAmount(), conn));
        if((withdraw.getAmount() + withdraw.getFee()) > mobileBalance(withdraw.getCreated_at(), conn)) throw new Exception("Votre solde est insuffisant pour effectuer cette opération");

        try {
             withdraw.insert(conn);
             conn.commit();
        } catch(IllegalAccessException | IllegalArgumentException | InvocationTargetException | SQLException ex) {
            conn.rollback();
            throw ex;
        } finally {
            
        }
    }
    
    private double getFeeAmount(double amount, Connection conn) throws SQLException, IllegalAccessException, IllegalArgumentException, InstantiationException, InvocationTargetException, NoSuchMethodException {
        double result = 0;
        List<Object> allFees = new Fee().getAllFees(null, conn);
        for(Object ob: allFees) {
            Fee fee = (Fee)ob;
            if(amount >= fee.getAmount_min() && amount <= fee.getAmount_max()) {
                result = fee.getAmount_fee();
                break;
            }
        }
        return result;
    }
    
    private Date lastCreditOpDate(Connection conn) throws SQLException {
        String req = String.format("select max(created_at) as max from mg.buyed_credits where customer_id = %d", getId());
         return FctGen.getDate(req, "max", conn);
    }
    
    private Date lastOperationDate(Date date, Connection conn) throws SQLException {
        String req = String.format("select *  from mg.all_customer_operations where customer_id= %d", getId());
        return FctGen.getDate(req, "created_at", conn);
    }
    
    public void deposit(Deposit deposit, Connection conn) throws SQLException, IllegalAccessException, InvocationTargetException, Exception {
        conn.setAutoCommit(false);
        Date lastOp = lastOperationDate(null, conn);
        if(lastOp != null)if(deposit.getCreated_at().compareTo(lastOp) < 0) throw new Exception("Veuillez vérifier la date de l'opération");
        
        try {
            deposit.insert(conn);
            conn.commit();
        } catch(IllegalAccessException | IllegalArgumentException | InvocationTargetException | SQLException ex) {
            conn.rollback();
            throw ex;
        } finally {
            
        }
        
    }
    
    public Customer find(String _phone_number, Connection conn) throws SQLException, InstantiationException, NoSuchMethodException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NotFoundException {
        Customer result = null;
        Object ob = FctGen.find(new Customer(), String.format("select * from %s where phone_number like '%s'", tableName(), _phone_number), columnsWithID(), conn);
        if(ob!=null) result = (Customer)ob;
        else {
            throw new NotFoundException("Ce numéro n'existe pas");
        }
        return result;
    }
    
    public Customer find(int _id, Connection conn) throws SQLException, InstantiationException, NoSuchMethodException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        Customer result = null;
        Object ob = FctGen.find(new Customer(), String.format("select * from %s where id = %d", tableName(), _id), columnsWithID(), conn);
        if(ob != null) result = (Customer)ob;
        return result;
    }
    
    public List<Object> findAll(Connection conn) throws SQLException, NoSuchMethodException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        List<Object> result = new ArrayList();
       
        result = FctGen.findAll(new Customer(), String.format("select * from %s", tableName()), columnsWithID() , conn);
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
