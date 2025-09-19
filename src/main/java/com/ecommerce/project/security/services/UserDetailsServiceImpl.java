package com.ecommerce.project.security.services;

import com.ecommerce.project.model.User;
import com.ecommerce.project.repository.UserRepository;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.transaction.Transactional;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Data
@NoArgsConstructor
@Service
public class UserDetailsServiceImpl implements UserDetailsService {
  private static final long serializeUUID = 1L;

  private Long id ;
  private String username ;
  private String email;

  @JsonIgnore
  private String password;

   Collection<? extends GrantedAuthority> authorities;

   @Autowired
    private UserRepository userRepository;



    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
     try {
         User user = userRepository.findByUsername(username)
                 .orElseThrow(()->
                         new UsernameNotFoundException("user Not Found with username : " + username)
                 )  ;
        return UserDetailsImpl.build(user);
     }catch(Exception e){
         System.out.println("failed to return data : " + e.getMessage());
          throw new UsernameNotFoundException("Error loading user: " + username, e);
     }

    }
}
