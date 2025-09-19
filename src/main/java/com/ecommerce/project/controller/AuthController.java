package com.ecommerce.project.controller;

import com.ecommerce.project.model.AppRole;
import com.ecommerce.project.model.Role;
import com.ecommerce.project.model.User;
import com.ecommerce.project.repository.RoleRepository;
import com.ecommerce.project.repository.UserRepository;
import com.ecommerce.project.security.jwt.JwtUtils;

import com.ecommerce.project.security.request.MessageResponse;
import com.ecommerce.project.security.request.SignUpRequest;
import com.ecommerce.project.security.request.UserInfoRequest;
import com.ecommerce.project.security.response.UserInfoResponse;
import com.ecommerce.project.security.services.UserDetailsImpl;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
   JwtUtils jwtUtils;

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    PasswordEncoder encoder;


    @PostMapping("/signIn")
    public ResponseEntity<?> signIn( @RequestBody UserInfoRequest userInfoRequest){
        Authentication authentication;


        try {
            authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            userInfoRequest.getUsername(), userInfoRequest.getPassword()
                    )
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);

            //generate the token for the response
            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

            String token = jwtUtils.generateJwtTokenFromUsername(userDetails);
            Map<String,Object> map = new HashMap<>();
            List<String> roles = userDetails.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority).toList();

            UserInfoResponse response = new UserInfoResponse(
                    userDetails.getId(), userDetails.getUsername(),token, roles
            );

            return  new ResponseEntity<>(response, HttpStatus.OK);
        }
        catch(AuthenticationException e){
            Map<String,Object> map = new HashMap<>();
            map.put("status", false);
            map.put("message", "Bad Credentials");
            return new  ResponseEntity<Object>(map, HttpStatus.UNAUTHORIZED);
        }

    }

    @PostMapping("/signup")
        public ResponseEntity<?> registerUser( @RequestBody SignUpRequest signUpRequest){
            if(userRepository.existsByUsername(signUpRequest.getUsername())){
                return ResponseEntity.badRequest().body(new MessageResponse("Error: User with username:" + signUpRequest.getUsername() + " already exist"));
            };

        if(userRepository.existsByEmail(signUpRequest.getEmail())){
            return ResponseEntity.badRequest().body(new MessageResponse("Error: User with email:" + signUpRequest.getEmail() + " already exist"));
        };

           User user = new User(
                   signUpRequest.getUsername(),
                   signUpRequest.getEmail(),
                   encoder.encode(signUpRequest.getPassword())

           ) ;

           Set<String> StrRoles = signUpRequest.getRoles();
           Set<Role> roles = new HashSet<>();

           if(StrRoles == null){
               Role userRole = roleRepository.findByRoleName(AppRole.ROLE_USER)
                       .orElseThrow(()-> new RuntimeException("Error: Role not Found"));

                 roles.add(userRole);
           }
           else{
             StrRoles.forEach(role ->{
             switch(role){
                 case "admin":  Role adminRole = roleRepository.findByRoleName(AppRole.ROLE_ADMIN)
                         .orElseThrow(()-> new RuntimeException("Error: Role not Found"));

                     roles.add(adminRole);
                     break;
                 case "seller": Role sellerRole = roleRepository.findByRoleName(AppRole.ROLE_SELLER)
                         .orElseThrow(()-> new RuntimeException("Error: Role not Found"));

                     roles.add(sellerRole);
                     break;
                 default:  Role userRole = roleRepository.findByRoleName(AppRole.ROLE_USER)
                         .orElseThrow(()-> new RuntimeException("Error: Role not Found"));

                     roles.add(userRole);
                  }

             });


           }
          user.setRoles(roles);

           userRepository.save(user);

           return new ResponseEntity <>("User registered successfully", HttpStatus.CREATED);



    }
}
