package com.ecommerce.project.security.jwt;

import com.ecommerce.project.model.User;
import com.ecommerce.project.repository.UserRepository;
import com.ecommerce.project.security.services.UserDetailsImpl;
import com.ecommerce.project.security.services.UserDetailsServiceImpl;
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

import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Optional;

@Component
public class AuthTokenFilter  extends OncePerRequestFilter {


    @Autowired
   private  JwtUtils jwtUtils;
    @Autowired
    private UserRepository userRepository;



    @Autowired
    private UserDetailsServiceImpl userDetailsService;


    private static final Logger logger = LoggerFactory.getLogger(AuthTokenFilter.class);
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        logger.debug("AuthFilter call for URI : {}", request.getRequestURL());
        try {

            String jwt = parseJwt(request);


            if(jwt != null && jwtUtils.validateJwtToken(jwt)) {

                logger.debug("AuthFilter call for URI : {}", this.jwtUtils);
                String userName = jwtUtils.getUsernameFromJwtToken(jwt);
                User user = userRepository.findByUsername(userName)
                        .orElseThrow(()->new RuntimeException("User not found"));



                UserDetailsImpl userDetails = UserDetailsImpl.build(user);

                System.out.println("Did userDetail build successfully ? :"  + userDetails);
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

       String jwt = jwtUtils.getJwtFromHeader(request)  ;

       logger.debug("AuthFilter.java : {} ", jwt);
       return jwt;
    }
}
