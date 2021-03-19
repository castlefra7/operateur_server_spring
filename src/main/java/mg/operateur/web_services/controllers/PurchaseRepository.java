/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mg.operateur.web_services.controllers;

import java.util.List;
import mg.operateur.business_logic.offer.Offer;
import mg.operateur.business_logic.offer.Purchase;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

/**
 *
 * @author dodaa
 */
public interface PurchaseRepository extends MongoRepository<Purchase, String> {
    
    @Query("{ 'customer_id' : ?0 }")
    public List<Purchase> findByCustomer_id(int customer_id);
    public Purchase findById(int id);
}
