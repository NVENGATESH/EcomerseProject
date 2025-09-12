package com.ecommerce.project.controller;

import com.ecommerce.project.config.AppConstants;
import com.ecommerce.project.model.Category;
import com.ecommerce.project.payload.CategoryDTO;
import com.ecommerce.project.payload.CategoryResponse;
import com.ecommerce.project.service.CategoryService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
@CrossOrigin(
    origins = {
        "https://eco-store-git-main-nvengateshs-projects.vercel.app/",
                "https://eco-store-five.vercel.app",
        "http://localhost:5173"
    },
    allowCredentials = "true"
)
@RestController
@RequestMapping("/api")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @GetMapping("/eco")
    public ResponseEntity<String> echomesage(@RequestParam(name = "mesage") String mesage){
        return  new ResponseEntity<>("Echoed Mesage"+mesage,HttpStatus.OK);
    }

//    @GetMapping("/public/categories")
//    //@RequestMapping(value = "/public/categories", method = RequestMethod.GET)
//    public ResponseEntity<CategoryResponse> getAllCategories() {
//        CategoryResponse categoryResponse = categoryService.getAllCategories();
//        return new ResponseEntity<>(categoryResponse, HttpStatus.OK);
//    }
//@GetMapping("/public/categories")
////@RequestMapping(value = "/public/categories", method = RequestMethod.GET)
//public ResponseEntity<CategoryResponse> getAllCategories(@RequestParam(name = "pageNumber",defaultValue = AppConstants.pageNumber,required = false) Integer pageNumber, @RequestParam(name = "pageSize",defaultValue = AppConstants.pageSize,required = false) Integer pageSize) {
//    CategoryResponse categoryResponse = categoryService.getAllCategories(pageNumber,pageSize);
//    return new ResponseEntity<>(categoryResponse, HttpStatus.OK);
//}
@GetMapping("/public/categories")
//@RequestMapping(value = "/public/categories", method = RequestMethod.GET)
public ResponseEntity<CategoryResponse> getAllCategories(@RequestParam(name = "pageNumber",defaultValue = AppConstants.pageNumber,required = false) Integer pageNumber, @RequestParam(name = "pageSize",defaultValue = AppConstants.pageSize,required = false) Integer pageSize
                                                         ,@RequestParam(name = "sortBy",defaultValue = AppConstants.SORT_CATEGORIES_BY,required = false)String sortBy,@RequestParam(name = "sortOrder",defaultValue = AppConstants.SORT_DIR,required = false)String sortOrder){
    CategoryResponse categoryResponse = categoryService.getAllCategories(pageNumber,pageSize,sortBy,sortOrder);
    return new ResponseEntity<>(categoryResponse, HttpStatus.OK);
}


    @PostMapping("/public/categories")
    //@RequestMapping(value = "/public/categories", method = RequestMethod.POST)
    public ResponseEntity<CategoryDTO> createCategory(@Valid @RequestBody CategoryDTO categoryDTO) {
      CategoryDTO categoryDTO1=  categoryService.createCategory(categoryDTO);
        return new ResponseEntity<>(categoryDTO1, HttpStatus.CREATED);
    }

    @DeleteMapping("/admin/categories/{categoryId}")
    public ResponseEntity<CategoryDTO> deleteCategory(@PathVariable Long categoryId) {

        CategoryDTO deletedCategory = categoryService.deleteCategory(categoryId);
        return new ResponseEntity<>(deletedCategory, HttpStatus.OK);

    }


    @PutMapping("/public/categories/{categoryId}")
    public ResponseEntity<CategoryDTO> updateCategory(@Valid @RequestBody CategoryDTO categoryDTO,
                                                 @PathVariable Long categoryId) {

        CategoryDTO savedCategoryDTO = categoryService.updateCategory(categoryDTO, categoryId);
        return new ResponseEntity<>(savedCategoryDTO , HttpStatus.OK);


    }

}
