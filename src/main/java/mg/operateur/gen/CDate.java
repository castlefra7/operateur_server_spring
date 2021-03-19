/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mg.operateur.gen;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 *
 * @author lacha
 */
public class CDate {
    public static SimpleDateFormat getDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd"); // TODO ADD HOURS AND MINUTES
        return sdf;
    }
    
    public static int numberSeconds(String hhMM) throws InvalidFormatException {
        String[] numb = hhMM.split(":");
        if(numb.length < 3) throw new InvalidFormatException("Format de date invalide. Doit-être de type hh:mm:ss");
        int result = Integer.parseInt(numb[1]) * 60;
        result += Integer.parseInt(numb[0]) * 60 * 60;
        result += Integer.parseInt(numb[2]);
        return result;
    }
    
    public static Date addDay(Date date, int number) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.DAY_OF_MONTH, number);
        return c.getTime();
    }
}
