/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mg.operateur.gen;

/**
 *
 * @author lacha
 */
public class InvalidDateException extends Exception {
    public InvalidDateException() {
        super("Veuillez vérifier la date");
    }
}
