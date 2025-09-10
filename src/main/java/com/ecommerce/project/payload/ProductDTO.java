package com.ecommerce.project.payload;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductDTO {

    private Long productId ;
    @NotBlank(message="Product name must not be empty")
    @Size(min=3, message="Product name must contain least 3 characters")
    private String productName;
    @NotBlank
    @Size(min=6, message="Product description must contain least 6 characters")
    private String description;
    private String image;
    private  Integer quantity ;
    private  Double  price;
    private  Double  discount;
    private  Double  specialPrice;
}
