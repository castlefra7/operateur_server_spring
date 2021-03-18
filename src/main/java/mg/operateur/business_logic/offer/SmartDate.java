/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mg.operateur.business_logic.offer;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 *
 * @author dodaa
 */
public class SmartDate extends Date{
    
    private static final String DATE_FORMAT = "yyyy-MM-dd";
    
    public SmartDate(long timeStamp) {
        this.setTime(timeStamp);
    }
    
    public SmartDate(String dateStr) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
        this.setTime(sdf.parse(dateStr).getTime());
    }
    
    public SmartDate addDays(int days) {
        Calendar c = Calendar.getInstance();
        c.setTime(this);
        c.add(Calendar.DAY_OF_YEAR, days);
        return new SmartDate(c.getTimeInMillis());
    }
    
    public static SmartDate now() {
        Calendar c = Calendar.getInstance();
        return new SmartDate(c.getTimeInMillis());
    }

    @Override
    public String toString() {
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
        return sdf.format(this);
    }
}
