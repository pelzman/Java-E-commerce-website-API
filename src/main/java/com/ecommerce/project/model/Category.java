package com.ecommerce.project.model;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity(name = "categories")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "category")
@ToString
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long categoryId;

     @NotBlank
    private String categoryName;

     @ToString.Exclude
     @OneToMany(mappedBy = "category")
    private List<Product> products = new ArrayList<>();

    public Category(String categoryName) {
        this.categoryName = categoryName;
    }
}


//    public Category(Long categoryId, String categoryName) {
//        CategoryId = categoryId;
//        CategoryName = categoryName;
//    }
//
//    public Category() {
//    }
//
//    public Long getCategoryId() {
//        return CategoryId;
//    }
//
//    public void setCategoryId(Long categoryId) {
//        CategoryId = categoryId;
//    }
//
//    public String getCategoryName() {
//        return CategoryName;
//    }
//
//    public void setCategoryName(String categoryName) {
//        CategoryName = categoryName;
//    }




