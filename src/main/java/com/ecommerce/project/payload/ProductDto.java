package com.ecommerce.project.payload;


import com.ecommerce.project.model.Category;
import com.ecommerce.project.model.Product;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductDto {


    private Long ProductId;
    private String productName;
    private byte[] imagedata;
    private String description;
    private Integer quantity;
    private double price;
    private double discount;
    private double specialPrice;
    private String brand;
    private Long categoryId;
    private Long CartId;



//
//    @JsonBackReference
//    private Category category;

}
