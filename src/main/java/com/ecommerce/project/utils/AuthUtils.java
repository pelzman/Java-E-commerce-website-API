package com.ecommerce.project.utils;

import com.ecommerce.project.exceptions.APIException;
import com.ecommerce.project.exceptions.ResourceNotFoundException;
import com.ecommerce.project.model.User;
import com.ecommerce.project.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
public class AuthUtils {

    @Autowired
    UserRepository userRepository;


    private String getAuthenticatedUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication == null ||
                !authentication.isAuthenticated() ||
                authentication instanceof AnonymousAuthenticationToken
        ){
            throw new UsernameNotFoundException("User not authenticated.");
        }

        return authentication.getName();
    }

    public String loggedInEmail (){

        String userName = getAuthenticatedUsername();

        User user =  userRepository.findByUsername(userName)
                .orElseThrow(()->new UsernameNotFoundException("User with username " + userName + " not found"));
        return user.getEmail();
    }

    public Long loggedInId (){
        String userName = getAuthenticatedUsername();
        User user = userRepository.findByUsername(userName)
                .orElseThrow(()-> new UsernameNotFoundException("User not Found for username " + userName));
        return user.getUserId();
    }

    public User loggedInUser (){
        String userName = getAuthenticatedUsername();
        return userRepository.findByUsername(userName)
                .orElseThrow(()-> new UsernameNotFoundException("User not Found for username " + userName ));
    }
}
