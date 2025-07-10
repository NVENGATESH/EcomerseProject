package com.ecommerce.project.payload;

import com.ecommerce.project.model.Product;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoryDTO {
    private  Long categoryId;
    private  String categoryName;


//    @JsonManagedReference
//    private List<Product> products;
}
