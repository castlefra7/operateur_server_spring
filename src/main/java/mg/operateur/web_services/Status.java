/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mg.operateur.web_services;

/**
 *
 * @author lacha
 */
public class Status {
    private int code;
    private String message;

    public Status() {
        setCode(200);
        setMessage("Succés");
    }
    
    public int getCode() {
        return code;
    }

    public final void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public final void setMessage(String message) {
        this.message = message;
    }
    
}
