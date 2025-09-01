package com.ecommerce.project.service;

import com.ecommerce.project.payload.CartDto;
import com.ecommerce.project.payload.CartDtoResponse;

import java.util.List;

public interface CartService {
    CartDto addCartByProductId(Long prouctId);

    CartDtoResponse getAllCarts();
}
