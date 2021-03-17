package mg.operateur.web_services.controllers;
import javax.servlet.http.HttpServletRequest;

public class AuthLogic {
    public String resolveToken(HttpServletRequest req) {
    String bearerToken = req.getHeader("Authorization");
    if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
      return bearerToken.substring(7);
    }
    return null;
  }
}