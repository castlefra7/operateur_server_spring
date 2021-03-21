/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mg.operateur.gen;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author lacha
 */
public class CustomDateFormat extends SimpleDateFormat {
    public CustomDateFormat(String _format) {
        super(_format);
    }
    
    @Override
    public Date parse(String  source) throws ParseException {
        if(!source.contains(":")) {
            source += " 00:00";
        }
        return super.parse(source);
    }
}
