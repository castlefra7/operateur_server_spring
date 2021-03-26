/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mg.operateur.web_services.controllers;

import java.util.Date;
import java.util.List;
import mg.operateur.business_logic.offer.Purchase;
import org.springframework.data.mongodb.repository.MongoRepository;


/**
 *
 * @author dodaa
 */

public interface PurchaseRepository extends MongoRepository<Purchase, String> {

    /**
     *
     * @param customer_id
     */

    @org.springframework.data.mongodb.repository.Query("{ 'customer_id' : ?0 }")
    public List<Purchase> findByCustomer_id(int customer_id);
    public Purchase findById(int id);
    @org.springframework.data.mongodb.repository.Query("{ 'endDate': {$gt: ?0 }, 'customer_id' : ?1 }")
    public List findByEndDateGreaterThanAndCustomer_id(Date date, int customer_id);
    
    /**
     *
     * @param date
     * @return
     */

}
