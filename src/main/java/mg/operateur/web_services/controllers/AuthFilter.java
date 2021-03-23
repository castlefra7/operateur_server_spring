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

        System.out.println("======Security Filtering=====");
        // System.out.println("Remote Host:"+request.getRemoteHost());
        // System.out.println("Remote Address:"+httpServletRequest.getRemoteAddr());
        String uri = httpServletRequest.getRequestURI();
        String[] splited = uri.split("/");

        if (splited.length >= 1) {
            String controller = splited[1];
            if (controller.equals("pricings") || controller.equals("stats") || controller.equals("pos") || controller.equals("customers")) {

                String token = authLogic.resolveToken(httpServletRequest);
                if (token == null) {
                    httpServletResponse.sendRedirect("/errors/tokens");
                    return;
                } else {
                    Jws<Claims> jws;
                    try {
                        jws = Jwts.parserBuilder()
                                .setSigningKey(Auth.getKey())
                                .build()
                                .parseClaimsJws(token);

                        if (controller.equals("pricings") || controller.equals("pos")) {
                            if (!jws.getBody().getSubject().equals("Admin")) {
                                throw new JwtException("admin");
                            }
                        }

                        httpServletRequest.setAttribute("id", jws.getBody().getSubject());
                    } catch (JwtException ex) {
                        if (ex.getMessage().contains("admin")) {
                            httpServletResponse.sendRedirect("/errors/notadmin");
                        } else {
                            httpServletResponse.sendRedirect("/errors/invalidtokens");
                        }
                        return;
                    }
                }

            }
        }
        System.out.println("=============================");
        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }
}
