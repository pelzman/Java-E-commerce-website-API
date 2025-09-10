package com.ecommerce.project.controller;

import com.ecommerce.project.config.AppConstant;
import com.ecommerce.project.exceptions.APIException;
import com.ecommerce.project.model.Category;
import com.ecommerce.project.payload.CategoryDTO;
import com.ecommerce.project.payload.CategoryResponse;
import com.ecommerce.project.service.CategoryService;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class CategoryController {
      private final CategoryService categoryService;
      @Autowired
      private ModelMapper modelMapper;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }




    @GetMapping("/public/categories")
    public ResponseEntity<CategoryResponse> getAllCategories(

            @RequestParam(name="pageNumber", defaultValue = AppConstant.PAGE_NUMBER, required = false) Integer pageNumber,
            @RequestParam(name="pageSize", defaultValue= AppConstant.PAGE_SIZE, required = false) Integer pageSize,
            @RequestParam(name="sortBy", defaultValue=AppConstant.SORT_CATEGORY_BY, required = false) String sortBy,
            @RequestParam(name="sortOrder", defaultValue=AppConstant.SORT_DIR, required = false) String sortOrder

    )  {
         CategoryResponse categoryResponse = categoryService.getAllCategories(pageNumber, pageSize, sortBy, sortOrder);
        return new ResponseEntity<>(categoryResponse, HttpStatus.OK);
    }
    @PostMapping("/public/categories")
    public ResponseEntity<CategoryDTO> createCategory(@Valid @RequestBody CategoryDTO categoryDTO){
         CategoryDTO saveCategoryDTO =  categoryService.createCategory(categoryDTO);

        return  new ResponseEntity<CategoryDTO>(saveCategoryDTO, HttpStatus.CREATED);
    }
    @DeleteMapping("/admin/categories/{categoryId}")
    public ResponseEntity<CategoryDTO>deleteCategory( @PathVariable Long categoryId) {

            CategoryDTO deletedCategory = categoryService.deleteCategory(categoryId);
        return new ResponseEntity<CategoryDTO>(deletedCategory,HttpStatus.OK);

    }

    @PutMapping("/public/categories/{categoryId}")
    public ResponseEntity<CategoryDTO> updateCategory( @Valid @RequestBody() CategoryDTO categoryDTO, @PathVariable Long categoryId){

            CategoryDTO savedCategory = categoryService.updateCategory(categoryDTO, categoryId) ;
            return new ResponseEntity<>(savedCategory, HttpStatus.OK);



    }

  @DeleteMapping("/admin/categories")
    public String deleteAllCategories(){
       categoryService.deleteAllCategories();
       return "";
    }


}
