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
    public static String customerRemainCallsInMess(HashMap<String, Double> response) {
        String result = "Vos restes sont: ";
        for(Map.Entry<String, Double> me: response.entrySet()) {
            String sentence = "";
            if(me.getKey().startsWith("i")) {
                sentence += "Internet [ " + me.getValue() + " Octets]. ";
            } else if(me.getKey().startsWith("m")) {
                sentence += "Message: [" +  me.getValue();
                if(me.getKey().toLowerCase().endsWith("ar")) {
                   sentence += " Ar].";
                } else {
                   sentence += " Nombre(s)]. ";
                }
            } else {
                sentence += "Appel: [" + me.getValue();
                if(me.getKey().toLowerCase().endsWith("ar")) {
                    sentence += " Ar]. ";
                } else {
                    sentence += " Secondes]. ";
                }
            }
            result += sentence;
        }
        return result;
    }
    public static String customerRemainOffer(HashMap<String, Date> response) {
        String result = "Vos offres actuels: ";
        for (Map.Entry<String, Date> me : response.entrySet()) {
            result += "L'offre " + me.getKey() + " qui se termine le " + CDate.format(me.getValue()) + ". ";
        }

        return result;
    }
}
