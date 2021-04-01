/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mg.operateur.business_logic.mobile_credit;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import mg.operateur.gen.CDate;

/**
 *
 * @author lacha
 */
public class ResponseMessage {
    public static String customerRemainOffer(HashMap<String, Date> response) {
        String result = "Vos offres actuels: ";
        for (Map.Entry<String, Date> me : response.entrySet()) {
            result += "L'offre " + me.getKey() + " qui se termine le " + CDate.format(me.getValue()) + ". ";
        }

        return result;
    }
}
