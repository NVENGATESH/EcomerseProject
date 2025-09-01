package com.ecommerce.project.service;

import com.ecommerce.project.model.Product;
import com.ecommerce.project.payload.ProductDto;
import com.ecommerce.project.payload.ProductResponse;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface ProductService {
    ProductDto addProduct(Long categoryId, ProductDto product);

    ProductResponse getAllProduct();

    ProductResponse searchByCategory(Long categoryId);

    ProductResponse searchByKeyWord(String keyword);

    ProductDto updateProduct(Long productId, ProductDto product)  ;

    ProductDto deleteProduct(Long productId);

    Product updateImage(Long productId, MultipartFile imagefile) throws IOException;

    Product getProductImageAll(Long productId);



    ProductDto addProductWithImage(Long categoryId, ProductDto productDto, MultipartFile imageFile) throws IOException;

    ProductDto updateProductWithImage(Long categoryId, ProductDto productDto, MultipartFile imageFile) throws IOException;

    ProductDto getAllProductid(Long productId);

    String getProductname(Long productId);

    String getProductBrand(Long productId);
}
