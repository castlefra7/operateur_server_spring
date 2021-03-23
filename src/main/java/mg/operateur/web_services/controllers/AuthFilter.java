package mg.operateur.web_services.controllers;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import mg.operateur.conn.Auth;
import org.springframework.web.filter.OncePerRequestFilter;

import org.springframework.stereotype.Component;

@Component
public class AuthFilter extends OncePerRequestFilter {

    private AuthLogic authLogic = new AuthLogic();

    @Override
    public void destroy() {
    }

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {

        // System.out.println("Remote Host:"+request.getRemoteHost());
        // System.out.println("Remote Address:"+httpServletRequest.getRemoteAddr());
        System.out.println("======Security Filtering=====");
        String uri = httpServletRequest.getRequestURI();
        String[] splited = uri.split("/");

        if (splited.length >= 1) {
            String controller = splited[1];
            if (controller.equals("pricings") || controller.equals("stats") ) {

                String token = authLogic.resolveToken(httpServletRequest);
                if (token == null) {
                    httpServletResponse.sendError(0, "Veuillez spécifier un token");
                    return;
                } else {
                    // validate the token
                    Jws<Claims> jws;
                    try {
                        jws = Jwts.parserBuilder()
                                .setSigningKey(Auth.getKey())
                                .build()
                                .parseClaimsJws(token);
                        //System.out.println(jws.getBody().getSubject() + " " + jws.getBody().getAudience()+ " " + jws.getBody().getIssuer());
                        if(controller.equals("pricings")) {
                            if(!jws.getBody().getSubject().equals("Admin")) {
                                throw new JwtException("Vous n'avez pas l'autorisation");
                            }
                        }
                        // TODO if it is /customers/callshistory, pass the  customer_id into the request
                    } catch (JwtException ex) {
                        httpServletResponse.sendError(0, "Votre token est invalide");
                        return;
                    }
                }

            }
        }
        System.out.println("=============================");
        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }
}
