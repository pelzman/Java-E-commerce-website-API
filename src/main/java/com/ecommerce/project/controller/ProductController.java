package com.ecommerce.project.controller;

import com.ecommerce.project.config.AppConstant;
import com.ecommerce.project.model.Product;
import com.ecommerce.project.payload.ProductResponse;
import com.ecommerce.project.payload.ProductDTO;
import com.ecommerce.project.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api")
public class ProductController {
      @Autowired
    public ProductService productService;

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/admin/categories/{categoryId}/product")
    public ResponseEntity<ProductDTO> addProduct(@Valid @RequestBody ProductDTO productDTO, @PathVariable Long categoryId){

      ProductDTO savedProduct =  productService.addProduct(categoryId, productDTO);
      return new ResponseEntity<>(savedProduct, HttpStatus.CREATED);
    }
   @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("/public/products")
    public ResponseEntity<ProductResponse> getAllProducts(
            @RequestParam(name="pageNumber", defaultValue = AppConstant.PAGE_NUMBER, required = false) Integer pageNumber,
             @RequestParam(name="pageSize",defaultValue = AppConstant.PAGE_SIZE, required = false) Integer pageSize,
                     @RequestParam(name="sortBy",defaultValue = AppConstant.SORT_PRODUCT_BY, required = false) String sortBy,
                     @RequestParam(name="sortOrder", defaultValue = AppConstant.SORT_DIR, required = false ) String sortOrder
    ){
        ProductResponse productResponse =  productService.getAllProducts(pageNumber, pageSize, sortBy, sortOrder);
        System.out.println("the controller is called");
        return new ResponseEntity<>(productResponse, HttpStatus.OK);
    }
    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("/public/categories/{categoryId}/products")
    public ResponseEntity<ProductResponse> getProductByCategory(@Valid @PathVariable Long categoryId,
       @RequestParam(name="pageNumber", defaultValue = AppConstant.PAGE_NUMBER, required = false) Integer pageNumber,
                                                                @RequestParam(name="pageSize",defaultValue = AppConstant.PAGE_SIZE, required = false) Integer pageSize,
                                                                @RequestParam(name="sortBy",defaultValue = AppConstant.SORT_PRODUCT_BY, required = false) String sortBy,
                                                                @RequestParam(name="sortOrder", defaultValue = AppConstant.SORT_DIR, required = false ) String sortOrder
    ) {

        ProductResponse productResponse = productService.getProductsByCategory(categoryId, pageNumber, pageSize, sortBy, sortOrder);
        return new ResponseEntity<>(productResponse, HttpStatus.OK);
    }
    @GetMapping("/public/products/keyword/{keyword}")
    public ResponseEntity<ProductResponse> getProductByKeyword( @PathVariable String keyword,
                                                                @RequestParam(name="pageNumber", defaultValue = AppConstant.PAGE_NUMBER, required = false) Integer pageNumber,
                                                                @RequestParam(name="pageSize",defaultValue = AppConstant.PAGE_SIZE, required = false) Integer pageSize,
                                                                @RequestParam(name="sortBy",defaultValue = AppConstant.SORT_PRODUCT_BY, required = false) String sortBy,
                                                                @RequestParam(name="sortOrder", defaultValue = AppConstant.SORT_DIR, required = false ) String sortOrder){
      ProductResponse productResponse = productService.searchProductByKeywords(keyword, pageNumber, pageSize, sortBy, sortOrder)  ;
      return new ResponseEntity<>(productResponse, HttpStatus.FOUND);
    }
    @PutMapping("/admin/products/{productId}")
    public ResponseEntity<ProductDTO> updateProduct( @Valid @PathVariable Long productId, @RequestBody ProductDTO productDTO) {
     ProductDTO updatedProduct =   productService.getUpdatedProduct(productId, productDTO);
        return new ResponseEntity<>(updatedProduct, HttpStatus.OK)  ;
    }

    @DeleteMapping("/admin/products/{productId}")
    public ResponseEntity<ProductDTO> deleteProduct(@PathVariable Long productId){

      ProductDTO productDTO =  productService.deleteProduct(productId);

      return new ResponseEntity<>(productDTO, HttpStatus.OK);

    }
    @PutMapping("/public/products/{productId}/image")
    public ResponseEntity<ProductDTO> uploadImageForProduct(@PathVariable Long productId, @RequestParam("image")MultipartFile image) throws IOException {

        ProductDTO updatedProduct = productService.uploadImage(productId, image);
        return new ResponseEntity<>(updatedProduct, HttpStatus.OK);

    }
}
