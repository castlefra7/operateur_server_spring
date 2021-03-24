/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mg.operateur.gen;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 *
 * @author lacha
 */
public class CDate {
    public static CustomDateFormat getDate() {
        CustomDateFormat sdf = new CustomDateFormat("yyyy-MM-dd HH:mm");
        return sdf;
    }
    
    public static Date endOfDay(Date date) {
        Calendar cal = new GregorianCalendar();
        cal.setTime(date);
        cal.set(Calendar.HOUR, 23);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.SECOND, 59);
        
        return cal.getTime();
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
