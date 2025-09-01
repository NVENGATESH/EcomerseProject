package com.ecommerce.project.payload;

import com.ecommerce.project.model.Product;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CartDto {

    private Long id;

    private Double totalPrice;
    private Integer count;
    private Boolean Clicked;
    private Boolean isAvailable;
    private Long productIds;

}
