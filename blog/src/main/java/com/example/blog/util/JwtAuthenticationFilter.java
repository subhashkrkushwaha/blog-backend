package com.example.blog.util;

import com.example.blog.service.UserDetailsServiceImpl;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JWTUtility jwtUtility;
    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)  throws ServletException, IOException {

               String authHeader = request.getHeader("Authorization");

               if(authHeader != null && authHeader.startsWith("Bearer ")){

                   String token = authHeader.substring(7);
                   try {
                    String email = jwtUtility.extractUserEmail(token);
                       if (email != null && SecurityContextHolder.getContext().getAuthentication() == null
                       && jwtUtility.validateToken(token)) {
                           UserDetails userDetails = userDetailsService.loadUserByUsername(email);
                           UsernamePasswordAuthenticationToken authentication =
                                   new UsernamePasswordAuthenticationToken( userDetails,
                                   null,
                                   userDetails.getAuthorities()
                                   );
                           SecurityContextHolder.getContext().setAuthentication(authentication);
                       }
                   }catch (Exception e){
                       System.out.println("JWT error: " + e.getMessage());
                   }
               }
        filterChain.doFilter(request, response);
    }

}
