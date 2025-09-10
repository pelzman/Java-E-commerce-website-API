package com.ecommerce.project.service;

import com.ecommerce.project.payload.ProductDTO;
import com.ecommerce.project.payload.ProductResponse;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface ProductService {

     ProductDTO addProduct( Long CategoryId, ProductDTO productDTO );

  ProductResponse getAllProducts(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder);

    ProductResponse getProductsByCategory(Long categoryId, Integer pageNumber, Integer pageSize, String sortBy, String sortOrder);

    ProductResponse searchProductByKeywords(String keyword, Integer pageNumber, Integer pageSize, String sortBy, String sortOrder);

    ProductDTO getUpdatedProduct(Long productId, ProductDTO productDTO);

    ProductDTO deleteProduct(Long productId);

  ProductDTO uploadImage(Long productId, MultipartFile image) throws IOException;
}
