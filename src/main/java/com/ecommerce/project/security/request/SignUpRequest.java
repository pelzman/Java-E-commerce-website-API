package com.ecommerce.project.security.request;

import com.ecommerce.project.model.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor

public class SignUpRequest {

    @NotBlank
    @Size(min=2, message="Username must be at least 5 characters")
    private String username;

    @NotBlank
    @Email
    private String email;

    @NotBlank
    @Size(  max=120, message="Password must be  at most 120 characters")
    private String password;

    @NotBlank
    private Set<String> roles;

    public SignUpRequest(String username, String email, String password, Set<String> roles) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.roles = roles;
    }
}
