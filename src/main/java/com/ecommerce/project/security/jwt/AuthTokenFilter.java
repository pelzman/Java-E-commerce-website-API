package com.ecommerce.project.security.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class AuthTokenFilter  extends OncePerRequestFilter {


    @Autowired
   private  JwtUtils jwtUtils;

    @Autowired
    private UserDetailsService userDetailsService;


    private static final Logger logger = LoggerFactory.getLogger(AuthTokenFilter.class);
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        logger.debug("AuthFilter call for URI : {}", request.getRequestURL());
        try {

            String jwt = parseJwt(request);


            if(jwt != null && jwtUtils.validateJwtToken(jwt)) {

                logger.debug("AuthFilter call for URI : {}", this.jwtUtils);
                String userName = jwtUtils.getUsernameFromJwtToken(jwt);
                UserDetails userDetails = userDetailsService.loadUserByUsername(userName);

                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());

// setting the  request details  for auditing purpose
                authentication.setDetails( new WebAuthenticationDetailsSource().buildDetails(request));
                // setting the user details to spring authentication context
               // tells the spring security  who the current user is


                SecurityContextHolder.getContext().setAuthentication(authentication);

                logger.debug("Role from JWT : {}", userDetails.getAuthorities());




            }

        } catch(Exception e){

            logger.error("User cannot be authenticated : {}", e.getMessage());
        }

        filterChain.doFilter(request, response);



    }

    private String parseJwt(HttpServletRequest request) {
        System.out.println(request.getHeader("Authorization"));
       String jwt = jwtUtils.getJwtFromHeader(request)  ;

       logger.debug("AuthFilter.java : {} ", jwt);
       return jwt;
    }
}
