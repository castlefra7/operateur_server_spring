/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mg.operateur.gen;

import java.text.SimpleDateFormat;

/**
 *
 * @author lacha
 */
public class CDate {
    public static SimpleDateFormat getDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd"); // TODO ADD HOURS AND MINUTES
        return sdf;
    }
}
