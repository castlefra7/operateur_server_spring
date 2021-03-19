/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mg.operateur.web_services.resources.commons;

/**
 *
 * @author lacha
 */
public class MessageJSON {
    private String message;

    public MessageJSON() {}
    
    public MessageJSON(String _message) {
        setMessage(_message);
    }
    
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
    
    
}
