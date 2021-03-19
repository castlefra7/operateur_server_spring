/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mg.operateur.web_services.controllers;

import java.util.List;
import mg.operateur.business_logic.offer.Purchase;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 *
 * @author dodaa
 */
public interface PurchaseRepository extends MongoRepository<Purchase, String> {
    
    public List<Purchase> findByCustomer_id(int customer_id);
}
