package mg.operateur.web_services.controllers;

import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.ParseException;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import mg.operateur.business_logic.mobile_credit.Credit;
import mg.operateur.business_logic.mobile_credit.Customer;
import mg.operateur.business_logic.mobile_credit.Deposit;
import mg.operateur.business_logic.mobile_credit.Fee;
import mg.operateur.business_logic.mobile_credit.Withdraw;
import mg.operateur.conn.ConnGen;
import mg.operateur.gen.CDate;
import mg.operateur.gen.InvalidAmountException;
import mg.operateur.web_services.ResponseBody;
import mg.operateur.web_services.resources.mobilemoney.DepositJSON;
import mg.operateur.web_services.resources.commons.TransferJSON;
import mg.operateur.web_services.resources.credit.CreditMobileJSON;
import mg.operateur.web_services.resources.mobilemoney.FeeJSON;
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
        
        @PostMapping(prefix+"/buycredit")
        public ResponseBody buyCredit(@RequestBody CreditMobileJSON _credit) {
            ResponseBody response = new ResponseBody();
            Connection conn = null;
            try {
                conn = ConnGen.getConn();
                Customer customer = new Customer().find(_credit.getPhone_number(), conn);
                Credit credit = new Credit();
                credit.setCreated_at(CDate.getDate().parse(_credit.getDate()));
                credit.setAmount(_credit.getAmount());
                credit.setCustomer_id(customer.getId());
                credit.setCustomer_source_id(customer.getId());
                
                customer.buyCreditFromMobile(credit, _credit.getPassword(), conn);
                response.getStatus().setMessage("Succés");
            } catch(Exception ex) {
                this.setError(response, ex);
                out(ex);
            } finally {
                try { if(conn!=null)conn.close();} catch(SQLException ex) {out(ex);}
            }
            return response;
        }
        
        @PostMapping(prefix+"/fees")
        public ResponseBody fees(@RequestBody FeeJSON _fee) {
            ResponseBody response = new ResponseBody();
            Connection conn = null;
            try {
                conn = ConnGen.getConn();
                Fee fee = new Fee();
                fee.setCreated_at(CDate.getDate().parse(_fee.getDate()));
                fee.setAmount_max(_fee.getAmount_max());
                fee.setAmount_min(_fee.getAmount_min());
                fee.setAmount_fee(_fee.getAmount_fee());
                
                fee.insert(conn);
                response.getStatus().setMessage("Succés");
            } catch(IllegalAccessException | IllegalArgumentException | InvocationTargetException | SQLException | ParseException | InvalidAmountException ex) {
                setError(response, ex);
                out(ex);
            } finally {
                try { if(conn!=null)conn.close();} catch(SQLException ex) {out(ex);}
            }
            return response;
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
                try { if(conn!=null)conn.close();} catch(SQLException ex) {out(ex);}
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
                
                customer.withdraw(withdraw, pwd, false, conn);
                response.getStatus().setMessage("Succés");
            } catch(Exception ex) {
                setError(response, ex);
                out(ex);
            } finally {
                try { if(conn!=null)conn.close();} catch(SQLException ex) {out(ex);}
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
                try { if(conn!=null)conn.close();} catch(SQLException ex) {out(ex);}
            }
            return response;
        }
        
}
