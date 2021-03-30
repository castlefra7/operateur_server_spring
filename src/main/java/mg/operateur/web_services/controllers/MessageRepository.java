/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mg.operateur.web_services.controllers;

import mg.operateur.business_logic.mobile_credit.MessageMongo;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 *
 * @author lacha
 */
public interface MessageRepository  extends MongoRepository<MessageMongo, String>{
    
}
