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
import java.util.TimeZone;

/**
 *
 * @author lacha
 */
public class CDate {
    public static CustomDateFormat getDate() {
        CustomDateFormat sdf = new CustomDateFormat("yyyy-MM-dd HH:mm");
        //TimeZone tana = TimeZone.getTimeZone("Indian/Antananarivo");
        //sdf.setTimeZone(tana);
        return sdf;
    }
    
    public static Date endOfDay(Date date) {
        //TimeZone tana = TimeZone.getTimeZone("Indian/Antananarivo");

        Calendar cal = new GregorianCalendar();
        //cal.setTimeZone(tana);
        cal.setTime(date);
        cal.set(Calendar.HOUR_OF_DAY, 23);
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
    
    public static String format(Date date) {
        Calendar c = new GregorianCalendar();
        c.setTime(date);
        String day = c.get(Calendar.DAY_OF_MONTH) <= 9 ? "0" + String.valueOf(c.get(Calendar.DAY_OF_MONTH)):  String.valueOf(c.get(Calendar.DAY_OF_MONTH));
        String month = (c.get(Calendar.MONTH)+1) <= 9 ? "0" + String.valueOf(c.get(Calendar.MONTH) + 1):  String.valueOf(c.get(Calendar.MONTH)+1);
        String hour = c.get(Calendar.HOUR_OF_DAY) <= 9 ? "0" + String.valueOf(c.get(Calendar.HOUR_OF_DAY)):  String.valueOf(c.get(Calendar.HOUR_OF_DAY));
        String minute = c.get(Calendar.MINUTE) <= 9 ? "0" + String.valueOf(c.get(Calendar.MINUTE)):  String.valueOf(c.get(Calendar.MINUTE));
        String result = String.format("%s/%s/%s à %s:%s", day, month, c.get(Calendar.YEAR), hour, minute);
        
        return result;
    }
}
