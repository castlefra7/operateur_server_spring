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
import org.springframework.web.bind.annotation.CrossOrigin;

@Component
public class AuthFilter extends OncePerRequestFilter {

    private AuthLogic authLogic = new AuthLogic();

    @Override
    public void destroy() {
    }

    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {

        System.out.println("======Security Filtering=====");
        // System.out.println("Remote Host:"+request.getRemoteHost());
        // System.out.println("Remote Address:"+httpServletRequest.getRemoteAddr());
        String uri = httpServletRequest.getRequestURI();
        String[] splited = uri.split("/");

        httpServletResponse.setHeader("Access-Control-Allow-Origin", "*");
        httpServletResponse.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
        httpServletResponse.setHeader("Access-Control-Max-Age", "3600");
        httpServletResponse.setHeader("Access-Control-Allow-Headers", "authorization, content-type, xsrf-token");
        httpServletResponse.addHeader("Access-Control-Expose-Headers", "xsrf-token");
        if ("OPTIONS".equals(httpServletRequest.getMethod())) {
            httpServletResponse.setStatus(HttpServletResponse.SC_OK);
        } else {
            if (splited.length >= 1) {
                String controller = splited[1];
                if (controller.equals("pricings") || controller.equals("stats") || controller.equals("pos") || controller.equals("customers")) {
                    String token = authLogic.resolveToken(httpServletRequest);
                    if (token == null) {
                        httpServletResponse.setStatus(500);
                        return;
                    } else {
                        Jws<Claims> jws;
                        try {
                            jws = Jwts.parserBuilder()
                                    .setSigningKey(Auth.getKey())
                                    .build()
                                    .parseClaimsJws(token);

                            if (controller.equals("pricings") || controller.equals("pos") || controller.equals("stats")) {
                                if (!jws.getBody().getSubject().equals("Admin")) {
                                    throw new JwtException("admin");
                                }
                            }
                            httpServletRequest.setAttribute("id", jws.getBody().getSubject());
                        } catch (JwtException ex) {
                            if (ex.getMessage().contains("admin")) {
                                httpServletResponse.setStatus(500);
                            } else {
                                httpServletResponse.setStatus(500);
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
}
