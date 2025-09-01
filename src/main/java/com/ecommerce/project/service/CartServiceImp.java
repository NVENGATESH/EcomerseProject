package com.ecommerce.project.service;

import com.ecommerce.project.eception.ResourceNotFoundException;
import com.ecommerce.project.model.Cart;
import com.ecommerce.project.model.Product;
import com.ecommerce.project.payload.CartDto;
import com.ecommerce.project.payload.CartDtoResponse;
import com.ecommerce.project.repositories.CartRepository;
import com.ecommerce.project.repositories.ProductRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CartServiceImp implements CartService{
    @Autowired
    private ModelMapper modelMapper;


    @Autowired
    private final ProductRepository productrepository;
    @Autowired
    private final CartRepository cartrepository;

    public CartServiceImp(ProductRepository productrepository, CartRepository cartrepository) {
        this.productrepository = productrepository;
        this.cartrepository = cartrepository;
    }

    @Override
    public CartDto addCartByProductId(Long productId) {
        Product product = productrepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "productId", productId));

        Cart cart = product.getCart();  // check if product already has a cart

        if (cart == null) {
            // Create new cart
            cart = new Cart();
            boolean available = product.getQuantity() >= 2;
            cart.setAvailable(available);
            cart.setClicked(true);
              cart.setProductIds(productId);
            cart.setCount(1);
            cart.setProducts(List.of(product));
            cart.setTotalPrice(product.getPrice());

            // IMPORTANT: link product back to this cart
            product.setCart(cart);

            cartrepository.save(cart);
        } else {
            // Update existing cart
            cart.setAvailable(true);
//            cart.setProducts(product);
            cart.setClicked(true);
            cart.setCount(cart.getCount() + 1);
            cart.setTotalPrice(cart.getTotalPrice() + product.getPrice());

            // if product isn’t in the cart yet, add it
            if (!cart.getProducts().contains(product)) {
                cart.getProducts().add(product);
                product.setCart(cart);
            }

            cartrepository.save(cart);
        }

        return modelMapper.map(cart, CartDto.class);
    }
    @Override
    public CartDtoResponse getAllCarts() {
        List<Cart> carts = cartrepository.findAll();


        // Map each Cart to CartDto
        List<CartDto> dtoList = carts.stream()
                .map(cart -> modelMapper.map(cart, CartDto.class))
                .toList();

        // Put the list inside the response wrapper
        CartDtoResponse response = new CartDtoResponse();
        response.setCartDtos(dtoList);

        return response;
    }


}
