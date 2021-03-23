/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mg.operateur.business_logic.mobile_credit;

import mg.operateur.gen.InvalidFormatException;

/**
 *
 * @author lacha
 */
public class PhoneNumber {
    public static String getPrefix(String phone_number) throws InvalidFormatException {
        if(phone_number.length() < 9) throw new InvalidFormatException("Le numéro est incorrect");
        
        if(phone_number.startsWith("0")) {
            return "+261" + phone_number.substring(1, 3);
        } else {
            return "+261" + phone_number.substring(4, 6);
        }
    }
    
    public static String getValidNumber(String phone_number) throws InvalidFormatException {
        String result = "";
        if(phone_number.startsWith("0")) {
            result = String.format("+261%s", phone_number.substring(1));
        } else if (phone_number.startsWith("+261")) {
            result = phone_number;
        } else {
            result = String.format("+261%s", phone_number);
        }
        return result;
    }
}
