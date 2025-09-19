package com.ecommerce.project.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name="addresses")
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="address_id")
    private Long addressId;

    @Column(name="street")
    @NotBlank
    @Size(min=4, message="Street name must be at least 4 characters")
    private String  street;

    @Column(name="building_name")
    @NotBlank
    @Size(min=4, message="Building name must be at least 4 characters")
    private String  buildingName;


    @Column(name="city")
    @NotBlank
    @Size(min=2, message="City name must be at least 2 characters")
    private String   city;

    @Column(name="state")
    @NotBlank
    @Size(min=3, message="State name must be at least 4 characters")
    private String   state;

    @Column(name="country")
    @NotBlank
    @Size(min=4, message="Country name must be at least 4 characters")
    private String   country;
    @Column(name="zip_code")
    @NotBlank
    @Size(min=6, message="Zip code  must be at least 6 characters")
    private String   zipCode;

     @ToString.Exclude
    @ManyToMany(mappedBy = "addresses")
    private List<User> users = new ArrayList<>();

    public Address(String zipCode, String street, String buildingName, String city, String state, String country) {
        this.zipCode = zipCode;
        this.street = street;
        this.buildingName = buildingName;
        this.city = city;
        this.state = state;
        this.country = country;
    }
}
