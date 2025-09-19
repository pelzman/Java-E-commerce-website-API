package com.ecommerce.project.payload;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {

    private Long userId;

    @NotBlank
    @Size(max=20, message=" Username must be at most 20 characters")
    private  String username ;

    @NotBlank
    @Size(max=50, message=" Email must be at most 50 characters")
    @Email
    private  String email ;


    @NotBlank
    @Size(max=120, message=" Password must be atmost 120 characters")
    private  String  password;


}
