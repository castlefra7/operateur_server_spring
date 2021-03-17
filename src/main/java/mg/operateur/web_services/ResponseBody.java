/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mg.operateur.web_services;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author lacha
 */
public class ResponseBody {
    private Status status;
    private List<Object> data;

    public ResponseBody() {
        setStatus(new Status());
        setData(new ArrayList());
    }
    
    public Status getStatus() {
        return status;
    }

    public final void setStatus(Status status) {
        this.status = status;
    }

    public List<Object> getData() {
        return data;
    }

    public final void setData(List<Object> data) {
        this.data = data;
    }
    
    
}
