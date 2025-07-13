package com.ecommerce.project.controller;

import com.ecommerce.project.model.Product;
import com.ecommerce.project.payload.ProductDto;
import com.ecommerce.project.payload.ProductResponse;
import com.ecommerce.project.service.ProductService;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api")
public class ProductController {

    @Autowired
    private ProductService service;

    @PostMapping("/admin/categories/{categoryId}/Product")
    public ResponseEntity<ProductDto> addProduct(@Valid  @RequestBody ProductDto productDto, @PathVariable Long categoryId){
      ProductDto savedProtuctdto=  service.addProduct(categoryId,productDto);

      return new ResponseEntity<>(savedProtuctdto, HttpStatus.CREATED);
    }


@GetMapping("/public/products")
    public  ResponseEntity<ProductResponse> getAllProduct(){

        ProductResponse productResponse=service.getAllProduct();
        return new ResponseEntity<>(productResponse,HttpStatus.OK);
    }

    @GetMapping("/public/categories/{categoryId}/products")
    public ResponseEntity<ProductResponse>  getAllProductByCategory(@PathVariable Long categoryId){

        ProductResponse productResponse=     service.searchByCategory(categoryId);
        return new ResponseEntity<>(productResponse,HttpStatus.OK);
    }
    @GetMapping("/public/products/keyword/{keyword}")
    public ResponseEntity<ProductResponse>  getProductByKeyword(@PathVariable String keyword){

        ProductResponse productResponse=     service.searchByKeyWord(keyword);
        return new ResponseEntity<>(productResponse,HttpStatus.FOUND);
    }
//    @PutMapping("/admin/products/{productId}")
//    public ResponseEntity<ProductDto>  updateProduct(@RequestBody ProductDto productDto,@PathVariable Long productId){
//
//
//        ProductDto updatingProductDto=  service.updateProduct(productId,productDto);
//        return new ResponseEntity<>(updatingProductDto,HttpStatus.OK);
//    }
@PutMapping("/admin/products/{productId}")
public ResponseEntity<ProductDto>  updateProduct(@Valid @RequestBody ProductDto productDto,@PathVariable Long productId ){


    ProductDto updatingProductDto=  service.updateProduct(productId,productDto);
    return new ResponseEntity<>(updatingProductDto,HttpStatus.OK);
}

    @DeleteMapping("/admin/products/{productId}")
    public ResponseEntity<ProductDto>  deleteProduct(@PathVariable Long productId){


        ProductDto seletedPrdoduct=  service.deleteProduct(productId);
        return new ResponseEntity<>(seletedPrdoduct,HttpStatus.OK);
    }

    @PutMapping("/admin/products/{productId}/image")
    public ResponseEntity<?> updateIamge(@PathVariable Long productId,@RequestParam("image") MultipartFile imagefile){

        try{
            Product product=service.updateImage(productId,imagefile);
            return new ResponseEntity<>(product,HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/public/product/{productId}/image")

    public ResponseEntity<byte[]> getImage(@PathVariable Long productId){

        Product product=service.getProductImageAll(productId);
        byte[] imagefile=product.getImageData();
//        return new ResponseEntity<>(imagefile,HttpStatus.OK);
        return ResponseEntity.ok()
                .contentType(MediaType.valueOf(product.getImageType()))
                .body(imagefile);
    }


//    Image addigng Prioducrt
//@PostMapping("/admin/categories/{categoryId}/products")
//public ResponseEntity<ProductDto> addProductWithImage(@Valid
//        @PathVariable Long categoryId,
//        @RequestPart("productDto") ProductDto productDto,
//        @RequestPart("image") MultipartFile imageFile) throws IOException {
@PostMapping(value = "/admin/categories/{categoryId}/products", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
public ResponseEntity<ProductDto> addProductWithImage(
        @PathVariable Long categoryId,
        @RequestPart("productDto") ProductDto productDto,
        @RequestPart("image") MultipartFile imageFile) throws IOException {
    ProductDto savedProductDto = service.addProductWithImage(categoryId, productDto, imageFile);
    return new ResponseEntity<>(savedProductDto, HttpStatus.CREATED);
}


}


//@PostMapping("/product")
//public ResponseEntity<?> addProduct(@RequestPart Product product,@RequestPart MultipartFile imageFile){
//
//    try {
//        Product product1=service.addProduct(product,imageFile);
//        return  new ResponseEntity<>(product1,HttpStatus.CREATED );
//    }
//    catch (Exception e){
//        return  new ResponseEntity<>(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
//    }

//}
