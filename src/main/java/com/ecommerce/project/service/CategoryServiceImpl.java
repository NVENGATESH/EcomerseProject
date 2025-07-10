package com.ecommerce.project.service;

import com.ecommerce.project.eception.ApiException;
import com.ecommerce.project.eception.ResourceNotFoundException;
import com.ecommerce.project.model.Category;
import com.ecommerce.project.payload.CategoryDTO;
import com.ecommerce.project.payload.CategoryResponse;
import com.ecommerce.project.repositories.CategoryRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CategoryServiceImpl implements CategoryService{


    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ModelMapper modelMapper;
//
//    @Override
//    public CategoryResponse getAllCategories() {
//      List<Category> categories= categoryRepository.findAll();
//      if (categories.isEmpty())
//          throw new ApiException("No categories created Till NOw");
//     List<CategoryDTO> categoryDTOS=categories.stream()
//             .map(category -> modelMapper.map(category, CategoryDTO.class))
//             .toList();
//     CategoryResponse categoryResponse = new CategoryResponse();
//     categoryResponse.setContent(categoryDTOS);
//
//        return categoryResponse;
//    }

//    @Override
//    public CategoryResponse getAllCategories(Integer pageNumber, Integer pageSize) {
//        Pageable pageDEtails= PageRequest.of(pageNumber,pageSize);
//        Page<Category> categoryPage = categoryRepository.findAll(pageDEtails);
//        List<Category> categories= categoryPage.getContent();//it return list of catogoreis
//        if (categories.isEmpty())
//            throw new ApiException("No categories created Till NOw");
//        List<CategoryDTO> categoryDTOS=categories.stream()
//                .map(category -> modelMapper.map(category, CategoryDTO.class))
//                .toList();
//        CategoryResponse categoryResponse = new CategoryResponse();
//        categoryResponse.setContent(categoryDTOS);
//        categoryResponse.setPageNumber(categoryPage.getNumber());
//        categoryResponse.setPageSize(categoryPage.getSize());
//        categoryResponse.setTotalElements(categoryPage.getTotalElements());
//        categoryResponse.setTotalPages(categoryPage.getTotalPages());
//        categoryResponse.setLastPage(categoryPage.isLast());
//
//
//        return categoryResponse;
//    }

    @Override
    public CategoryResponse getAllCategories(Integer pageNumber, Integer pageSize,String sortBy,String sortOrder) {
        Sort sortByAnyOrder= sortOrder.equalsIgnoreCase("asc")
                ?Sort.by(sortBy).ascending():Sort.by(sortBy).descending();
        Pageable pageDEtails= PageRequest.of(pageNumber,pageSize,sortByAnyOrder);
        Page<Category> categoryPage = categoryRepository.findAll(pageDEtails);
        List<Category> categories= categoryPage.getContent();//it return list of catogoreis
        if (categories.isEmpty())
            throw new ApiException("No categories created Till NOw");
        List<CategoryDTO> categoryDTOS=categories.stream()
                .map(category -> modelMapper.map(category, CategoryDTO.class))
                .toList();
        CategoryResponse categoryResponse = new CategoryResponse();
        categoryResponse.setContent(categoryDTOS);
        categoryResponse.setPageNumber(categoryPage.getNumber());
        categoryResponse.setPageSize(categoryPage.getSize());
        categoryResponse.setTotalElements(categoryPage.getTotalElements());
        categoryResponse.setTotalPages(categoryPage.getTotalPages());
        categoryResponse.setLastPage(categoryPage.isLast());


        return categoryResponse;
    }


    @Override
    public CategoryDTO createCategory(CategoryDTO categoryDTO) {
        Category category=modelMapper.map(categoryDTO, Category.class);
        Category categoryFromDb=categoryRepository.findByCategoryName(category.getCategoryName());
       if (categoryFromDb!=null){
           throw new ApiException("Category with the name "+category.getCategoryName()+" already exists!!!");
       }

       
     Category savedCategory=   categoryRepository.save(category);
       CategoryDTO saveddCategoryDto=modelMapper.map(savedCategory,CategoryDTO.class);
         return saveddCategoryDto;

    }

    @Override
    public CategoryDTO deleteCategory(Long categoryId) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "caegoryId", categoryId));

        categoryRepository.delete(category);
//        return "Category with categoryId: " + categoryId + " deleted successfully !!";
       return modelMapper.map(category, CategoryDTO.class);
    }

    @Override
    public CategoryDTO updateCategory(CategoryDTO categoryDTO, Long categoryId) {

        Category savedCategory = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "caegoryId", categoryId));
        Category category=modelMapper.map(categoryDTO, Category.class);
        category.setCategoryId(categoryId);
        savedCategory = categoryRepository.save(category);

        return  modelMapper.map(savedCategory, CategoryDTO.class);
    }
}
