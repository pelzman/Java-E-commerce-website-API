package com.ecommerce.project.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor

@Table(name="products")
@ToString
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long productId;
    @NotBlank
    @Size
    private String productName ;
    @NotBlank(message = "Product name can not be empty")
    @Size
    private String description;
    private String  image;
    private Integer quantity;
    private Double price;
    private Double  discount;
    private Double specialPrice;

    @ToString.Exclude
    @ManyToOne
    @JoinColumn(name = "category_id")

    private Category category;
    @ToString.Exclude
    @ManyToOne
    @JoinColumn(name="seller_id")
    private User user;

    @OneToMany(mappedBy = "product", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.EAGER)
    private List<CartItem> products = new ArrayList<>();

    public Product(String productName, String description, String image, Integer quantity, Double price, Double discount, Double specialPrice, Category category) {
        this.productName = productName;
        this.description = description;
        this.image = image;
        this.quantity = quantity;
        this.price = price;
        this.discount = discount;
        this.specialPrice = specialPrice;
        this.category = category;
    }

}
