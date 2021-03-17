package mg.operateur.web_services.controllers;

import java.sql.Connection;
import java.sql.SQLException;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import java.util.concurrent.atomic.AtomicLong;
import org.springframework.web.bind.annotation.RequestParam;
import java.util.*;
import mg.operateur.business_logic.mobile_credit.Customer;
import mg.operateur.business_logic.mobile_credit.Deposit;
import mg.operateur.business_logic.mobile_credit.Withdraw;
import mg.operateur.conn.ConnGen;
import mg.operateur.gen.CDate;
import mg.operateur.web_services.ResponseBody;
import mg.operateur.web_services.resources.mobilemoney.DepositJSON;
import mg.operateur.web_services.resources.commons.TransferJSON;
import mg.operateur.web_services.resources.mobilemoney.WithdrawJSON;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@CrossOrigin(origins = "*")
@RestController
public class MobileMoneyController {
        private static final String prefix = "/mobilemoney";
        
        private void out(Exception ex) {
            ex.printStackTrace();
            System.out.println(ex.getMessage());
        }
        
        private void setError(ResponseBody response, Exception ex) {
            response.getStatus().setCode(500);
            response.getStatus().setMessage(ex.getMessage());
        }
        
	@RequestMapping(prefix)
	public ResponseBody index() {
            ResponseBody response = new ResponseBody();
            response.getStatus().setMessage("Mobile money");
            return response;
	}
        
        @PostMapping(prefix+"/deposit")
        public ResponseBody deposit(@RequestBody DepositJSON _deposit) {
            ResponseBody response = new ResponseBody();
            
            Connection conn = null;
            try {
                conn = ConnGen.getConn();
                Customer customer;
                customer = new Customer().find(_deposit.getPhone_number(), conn);
                
                Deposit deposit = new Deposit();
                deposit.setCreated_at(CDate.getDate().parse(_deposit.getDate()));
                deposit.setCustomer_id(customer.getId());
                deposit.setCustomer_source_id(customer.getId());
                deposit.setAmount(_deposit.getAmount());
                
                customer.deposit(deposit, conn);
                response.getStatus().setMessage("Succés");
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
        
        @PostMapping(prefix+"/withdraw")
        public ResponseBody withdraw(@RequestBody WithdrawJSON _withdraw) {
            ResponseBody response = new ResponseBody();
            Connection conn = null;
            try {
                conn = ConnGen.getConn();
                Customer customer;
                customer = new Customer().find(_withdraw.getPhone_number(), conn);
                
                Withdraw withdraw = new Withdraw();
                withdraw.setCreated_at(CDate.getDate().parse(_withdraw.getDate()));
                
                withdraw.setAmount(_withdraw.getAmount());
                withdraw.setCustomer_id(customer.getId());
                String pwd = _withdraw.getPassword();
                
                customer.withdraw(withdraw, pwd, conn);
                response.getStatus().setMessage("Succés");
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
        
        @PostMapping(prefix+"/transfer")
        public ResponseBody transfer(@RequestBody TransferJSON _transfer) {
            ResponseBody response = new ResponseBody();
            Connection conn = null;
            try {
                conn = ConnGen.getConn();
                Customer customer;
                customer = new Customer().find(_transfer.getPhone_number(), conn);
                Customer customerDest = new Customer().find(_transfer.getPhone_number_destination(), conn);
                String pwd = _transfer.getPassword();
                
                customer.transfer(_transfer.getAmount(), pwd, CDate.getDate().parse(_transfer.getDate()), customerDest.getId(), conn);
                response.getStatus().setMessage("Succés");
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
