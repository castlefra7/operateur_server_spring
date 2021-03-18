package mg.operateur.web_services.controllers;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.filter.OncePerRequestFilter;


import org.springframework.stereotype.Component;

@Component
public class AuthFilter extends OncePerRequestFilter  {
   private AuthLogic authLogic = new AuthLogic();

   @Override
   public void destroy() {}

   @Override
   protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
      
      // System.out.println("Remote Host:"+request.getRemoteHost());
      // System.out.println("Remote Address:"+httpServletRequest.getRemoteAddr());
        System.out.println("======Security Filtering=====");
        //System.out.println(authLogic.resolveToken(httpServletRequest));
        System.out.println("=============================");
        filterChain.doFilter(httpServletRequest, httpServletResponse);
   }
}