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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import mg.operateur.gen.NotFoundException;

/**
 *
 * @author lacha
 */
public class Customer extends Person {
    
    public Customer(){}
    public Customer(int _id) {
        setId(_id);
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
        if(!currCust.getPassword().equals(pwd)) throw new Exception("Mot de passe incorrect");
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
    
    public void buyCredit(Credit credit, Connection conn) throws SQLException, Exception {
        Date lastOp = lastCreditOpDate(conn);
        if(lastOp!=null) if(credit.getCreated_at().compareTo(lastOp) <0) throw new Exception("Veuillez vérifier la date de l'opération");
        conn.setAutoCommit(false);
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
            withdraw(_withdraw, pwd, conn);
            Customer newCust = new Customer(_customer_dest_id);
            newCust.deposit(_deposit, conn);
            
            conn.commit();
        } catch(IllegalAccessException | IllegalArgumentException | InvocationTargetException | SQLException ex) {
            conn.rollback();
            throw ex;
        } finally {
            
        }
    }
      
    public void withdraw(Withdraw withdraw, String pwd, Connection conn) throws SQLException, InstantiationException, NoSuchMethodException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, Exception {
        conn.setAutoCommit(false);
        Date lastOp = lastOperationDate(null, conn);
        if(lastOp != null)if(withdraw.getCreated_at().compareTo(lastOp) < 0) throw new Exception("Veuillez vérifier la date de l'opération");

        Customer currCust = find(getId(), conn);
        if(!currCust.getPassword().equals(pwd)) throw new Exception("Mot de passe incorrect");

        withdraw.setFee(getFeeAmount(withdraw.getAmount(), conn));
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
