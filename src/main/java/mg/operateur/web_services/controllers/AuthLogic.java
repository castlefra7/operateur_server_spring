package mg.operateur.web_services.controllers;

import java.util.Enumeration;
import javax.servlet.http.HttpServletRequest;

public class AuthLogic {

    public String resolveToken(HttpServletRequest req) {
        String bearerToken = req.getHeader("Authorization");
        /*Enumeration<String> headerNames = req.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            System.out.println("header :" + headerNames.nextElement());
        }*/
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}
