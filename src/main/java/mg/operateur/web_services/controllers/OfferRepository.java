/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mg.operateur.web_services.controllers;

/**
 *
 * @author dodaa
 */
import java.util.List;
import mg.operateur.business_logic.offer.Offer;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

public interface OfferRepository extends MongoRepository<Offer, String> {
    public Offer findById(int id);
}
