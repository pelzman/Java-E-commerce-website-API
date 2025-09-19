package com.ecommerce.project.security.services;

import com.ecommerce.project.model.Role;
import com.ecommerce.project.model.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor

public class UserDetailsImpl implements UserDetails {
    private  static final long serialVersionUUID = 1L;
    private Long id ;
    private String username;
    private String email;

    @JsonIgnore
    private String password;

    private Collection<? extends GrantedAuthority>authorities;
    public UserDetailsImpl(Long id, String username, String email, String password,
                           Collection<? extends GrantedAuthority> authorities) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.password = password;
        this.authorities = authorities;
    }

    // build the userDetail object

    public  static UserDetailsImpl build(User user){
        // getting the list of  authorities

        List<GrantedAuthority> authorities = user.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority(role.getRoleName().name()))
                .collect(Collectors.toList());

        //constructing the userDetails object
       return new UserDetailsImpl(
               user.getUserId(),
               user.getUsername(),
               user.getEmail(),
               user.getPassword(),
               authorities

       );
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }



@Override
    public boolean equals(Object o){
   if(this == o) return true;
   if(o == null || getClass() != o.getClass())
       return false;
   //casting Object o to UserDetailsIml type to get access to its field
   UserDetailsImpl user = (UserDetailsImpl) o ;
   return Objects.equals(id, user.id);
}

}
