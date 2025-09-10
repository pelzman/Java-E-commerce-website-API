package com.ecommerce.project.service;


import com.ecommerce.project.exceptions.APIException;
import com.ecommerce.project.exceptions.MyGlobalExceptionHandler;
import com.ecommerce.project.exceptions.ResourceNotFoundException;
import com.ecommerce.project.model.Category;
import com.ecommerce.project.payload.CategoryDTO;
import com.ecommerce.project.payload.CategoryResponse;
import com.ecommerce.project.repositpries.CategoryRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryServiceImpl implements CategoryService {
//    private List<Category> categories = new ArrayList<>();
//    private Long nextId = 1L;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public CategoryResponse getAllCategories(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder)  {
        Sort sortAndOrderBy = sortOrder.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageDetails = PageRequest.of(pageNumber, pageSize, sortAndOrderBy) ;
        Page<Category> categoryPage =  categoryRepository.findAll(pageDetails);

        List<Category> categories =  categoryPage.getContent();
        if(categories.isEmpty()) throw new APIException("No category is available");
        List<CategoryDTO> categoryDTOS =  categories.stream()
                .map(category -> modelMapper.map(category, CategoryDTO.class))
                .toList();
        CategoryResponse categoryResponse = new CategoryResponse();
        categoryResponse.setContent(categoryDTOS);
        categoryResponse.setPageNumber(categoryPage.getNumber());
        categoryResponse.setPageSize(categoryPage.getSize());
        categoryResponse.setTotalElements(categoryPage.getTotalElements());
        categoryResponse.setTotalPages(categoryPage.getTotalPages());
        categoryResponse.setLastPage(categoryPage.isLast());
        return  categoryResponse;
    }

    @Override
    public CategoryDTO createCategory(CategoryDTO categoryDTO) {
      Category category = modelMapper.map(categoryDTO, Category.class);
      Category CategoryFromDb = categoryRepository.findByCategoryName(category.getCategoryName());

      if(CategoryFromDb != null)
          throw  new APIException("Category with category name :" + category.getCategoryName() + " already exist !!!");
         Category savedCategory = categoryRepository.save(category);

        return modelMapper.map(savedCategory, CategoryDTO.class);
    }

    @Override
    public CategoryDTO deleteCategory(Long categoryId) {
         Category category = categoryRepository.findById(categoryId)
                 .orElseThrow(()-> new ResourceNotFoundException("Category","categoryId", categoryId ));
        categoryRepository.delete(category);

        return modelMapper.map(category , CategoryDTO.class);
    }

    @Override
    public CategoryDTO updateCategory(CategoryDTO categoryDTO, Long categoryId) {

         Optional<Category> savedCategoryOptional = categoryRepository.findById(categoryId) ;

          Category savedCategory = savedCategoryOptional
                              .orElseThrow(()-> new ResourceNotFoundException("Category", "categoryId", categoryId));

        savedCategory.setCategoryId(categoryId);
        savedCategory.setCategoryName(categoryDTO.getCategoryName());
         Category updatedCategory =  categoryRepository.save(savedCategory) ;

           return modelMapper.map(updatedCategory, CategoryDTO.class);




    }

    @Override
    public void deleteAllCategories() {
      categoryRepository.deleteAll();
    }
}
