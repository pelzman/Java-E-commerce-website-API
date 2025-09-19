package com.ecommerce.project.model;

import jakarta.persistence.*;
import jakarta.validation.Constraint;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.engine.internal.Cascade;

import java.util.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Table(name="users",
      uniqueConstraints = {
        @UniqueConstraint(columnNames = "username"),
              @UniqueConstraint(columnNames = "email")
      }
)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="user_id")
    private Long userId;

    @NotBlank
    @Size(max=30)
    @Column(name="username")
    private  String username ;

    @NotBlank
    @Column(name="email")
    @Email
    private  String email ;

    @NotBlank
    @Size(max=120)
    @Column(name="password")
    private  String  password;


    public User( String username, String email, String password ) {
        this.username = username;
        this.email = email;
        this.password = password;

    }

    @Setter
    @Getter
// IF YOU  WANT THE HIBERNATE TO HANDLE THE BIDIRECTIONAL RELATIONSHIP AUTOMATICALLY USE CASCADE TYPE.ALL
    @ToString.Exclude
    @ManyToMany(cascade={CascadeType.MERGE}, fetch = FetchType.LAZY)
    @JoinTable(
            name ="user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
             inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles = new HashSet<>();
    @Setter
    @Getter
    @ToString.Exclude
    @ManyToMany(fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
            name ="user_address",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "address_id")
    )


    private List<Address> addresses = new ArrayList<>();

    @ToString.Exclude
    @OneToMany(mappedBy = "user", cascade = {CascadeType.PERSIST, CascadeType.MERGE},
            orphanRemoval = true
    )
    private Set<Product> products;

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(userId, user.userId);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(userId);
    }

    public void addRole(Role role){
        this.roles.add(role);

    }



    // Safe toString without collections
    @Override
    public String toString() {
        return "User{" +
                "userId=" + userId +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
