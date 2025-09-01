package com.ecommerce.project.payload;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CartDtoResponse {
    List<CartDto> cartDtos;
}
