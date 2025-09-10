package com.ecommerce.project.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name="roles")
@ToString
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="role_id")
    private Integer roleId;

    @Enumerated(EnumType.STRING)
    @Column(length=20 , name="role_name")
    @ToString.Exclude
    private AppRole roleName;


    public Role(AppRole roleName) {
        this.roleName = roleName;
    }

     @ManyToMany(mappedBy = "roles")
    private Set<User> users = new HashSet<>();
}
