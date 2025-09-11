package com.ecommerce.project.repositpries;

import com.ecommerce.project.model.Category;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<Category , Long> {
    Category findByCategoryName(String CategoryName);
}
