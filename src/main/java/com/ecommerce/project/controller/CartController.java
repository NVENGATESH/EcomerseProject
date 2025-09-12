package com.ecommerce.project.controller;

import com.ecommerce.project.payload.CartDto;
import com.ecommerce.project.payload.CartDtoResponse;
import com.ecommerce.project.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
public class CartController {

    @Autowired
    private CartService service;

    @PostMapping("/admin/carts/{productId}")
    public ResponseEntity<CartDto> addCartByProductId(@PathVariable Long productId) {
        CartDto cartDto = service.addCartByProductId(productId);
        return ResponseEntity.ok(cartDto);
    }
    @GetMapping("/public/carts")
    public ResponseEntity<CartDtoResponse> getAllCart() {
        CartDtoResponse cartDtos =  service.getAllCarts();
        return ResponseEntity.ok(cartDtos);
    }



}
