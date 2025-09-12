package com.ecommerce.project.service;

import com.ecommerce.project.eception.ApiException;
import com.ecommerce.project.eception.ResourceNotFoundException;
import com.ecommerce.project.model.Cart;
import com.ecommerce.project.model.Category;
import com.ecommerce.project.model.Product;
import com.ecommerce.project.payload.ProductDto;
import com.ecommerce.project.payload.ProductResponse;
import com.ecommerce.project.repositories.CartRepository;
import com.ecommerce.project.repositories.CategoryRepository;
import com.ecommerce.project.repositories.ProductRepository;
import org.springframework.transaction.annotation.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductServiceImp  implements ProductService{
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private CartRepository cartrepository;


    @Autowired
    private ModelMapper modelMapper;

    @Override
    public ProductDto addProduct(Long categoryId, ProductDto productDto) {
        Category  category=categoryRepository.findById(categoryId)
                .orElseThrow(()-> new ResourceNotFoundException("category","categoryId",categoryId));

        boolean isProductNotPresent=true;
        List<Product> products=category.getProducts();
        for (Product value:products) {
            if (value.getProductName().equals(productDto.getProductName())) {
                isProductNotPresent=false;
                break;
            }

        }
        if (isProductNotPresent) {

            Product product = modelMapper.map(productDto, Product.class);

            product.setCategory(category);
//        product.setImage("Default.png");
            double speacialPrice = product.getPrice() - ((product.getDiscount() * 0.01) * product.getPrice());
            product.setSpecialPrice(speacialPrice);

            Product savedProduct = productRepository.save(product);
            return modelMapper.map(savedProduct, ProductDto.class);

        }else {
            throw  new ApiException("Product already exists");
        }


    }


    @Override
    public ProductResponse getAllProduct() {

      List<Product> products=productRepository.findAll();
      List<ProductDto> productDtos=  products.stream().map(product ->  modelMapper.map(product,ProductDto.class)).collect(Collectors.toList());

      ProductResponse  productResponse=new ProductResponse();
      productResponse.setContent(productDtos);
        return productResponse;
    }

    @Override
    public ProductResponse searchByCategory(Long categoryId) {
        Category  category=categoryRepository.findById(categoryId)
                .orElseThrow(()-> new ResourceNotFoundException("category","categoryId",categoryId));

        List<Product> products = productRepository.findByCategoryOrderByPriceAsc(category);

        List<ProductDto> productDtos=  products.stream().map(product ->  modelMapper.map(product,ProductDto.class)).collect(Collectors.toList());

        ProductResponse  productResponse=new ProductResponse();
        productResponse.setContent(productDtos);
        return productResponse;
    }

    @Override
    public ProductResponse searchByKeyWord(String keyword) {
        return null;
    }



    @Override
    @Transactional(readOnly = true)
    public ProductResponse searchProductByKeyword(String keyword) {
        List<Product> products = productRepository
                .findByProductNameContainingIgnoreCaseOrDescriptionContainingIgnoreCase(keyword, keyword);

        if (products.isEmpty()) {
            throw new ApiException("Products not found with keyword: " + keyword);
        }

        List<ProductDto> productDTOS = products.stream()
                .map(product -> modelMapper.map(product, ProductDto.class))
                .toList();

        ProductResponse productResponse = new ProductResponse();
        productResponse.setContent(productDTOS);

        return productResponse;
    }




    @Override
    public ProductDto updateProduct(Long productId, ProductDto productDto)  {
//        Get the Excisting product from Db
        Product productFromDb=productRepository.findById(productId).orElseThrow(()->new ResourceNotFoundException("product","productId",productId));

        Product product=modelMapper.map(productDto,Product.class);

//UpDATE the producr info with the one request body
        productFromDb.setProductName(product.getProductName());
        productFromDb.setDescription(product.getDescription());
        productFromDb.setQuantity(product.getQuantity());
        productFromDb.setDiscount(product.getDiscount());
        productFromDb.setPrice(product.getPrice());
        productFromDb.setSpecialPrice(product.getSpecialPrice());


//        Save to database
       Product savedProduct= productRepository.save(productFromDb);

        return modelMapper.map(savedProduct,ProductDto.class);
    }

    @Override
    public ProductDto deleteProduct(Long productId) {

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("product", "productId", productId));

        // Break relationship with Cart if exists

        Cart cart = product.getCart();
//        cart.getId();
        if (cart != null) {
            Long id=  cart.getId();
            Cart cart1 = cartrepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Cart", "CartId", id));



          cart1.setClicked(false);
            cart1.setCount(0);
            cart1.setAvailable(false);
            cart.getProducts().remove(product); // remove product from cart's list
            product.setCart(null);
            cartrepository.delete(cart1);
        }

        // Map to DTO before deletion (optional)
        ProductDto dto = modelMapper.map(product, ProductDto.class);

        // Delete the product
        productRepository.delete(product);

        return dto;
    }

    @Override
    public Product updateImage(Long productId, MultipartFile imagefile) throws IOException {

        Product product=productRepository.findById(productId).orElseThrow(()->new ResourceNotFoundException("product","productId",productId));
        product.setImageName(imagefile.getOriginalFilename());
        product.setImageType(imagefile.getContentType());
        product.setImageData(imagefile.getBytes());
        productRepository.save(product);
        return product;
    }

    @Override
    public Product getProductImageAll(Long productId) {

        Product product=productRepository.findById(productId).orElseThrow(()->new ResourceNotFoundException("product","productId",productId));
        return product;
    }



@Override
public ProductDto addProductWithImage(Long categoryId, ProductDto productDto, MultipartFile imageFile) throws IOException {
    Category category = categoryRepository.findById(categoryId)
            .orElseThrow(() -> new ResourceNotFoundException("Category", "categoryId", categoryId));

    Product product = modelMapper.map(productDto, Product.class);
    product.setCategory(category);

    if (imageFile != null) {
        product.setImageName(imageFile.getOriginalFilename());
        product.setImageType(imageFile.getContentType());
        product.setImageData(imageFile.getBytes());
    }

    Product savedProduct = productRepository.save(product);
    return modelMapper.map(savedProduct, ProductDto.class);
}

    @Override
    public ProductDto updateProductWithImage(Long ProductId, ProductDto productDto, MultipartFile imageFile) throws IOException {
        Product product1 = productRepository.findById(ProductId)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "productId", ProductId));

        // ✅ Fetch category from DB if provided in DTO
//    if (productDto.ge() != null) {
//        Category category = categoryRepository.findById(productDto.getCategoryId())
//                .orElseThrow(() -> new ResourceNotFoundException("Category", "id", productDto.getCategoryId()));
//        product1.setCategory(category);
//    }/

        // ✅ Update other fields
        product1.setProductName(productDto.getProductName());
        product1.setDescription(productDto.getDescription());
        product1.setQuantity(productDto.getQuantity());
        product1.setDiscount(productDto.getDiscount());
        product1.setPrice(productDto.getPrice());
        product1.setSpecialPrice(productDto.getSpecialPrice());
        product1.setBrand(productDto.getBrand());

        // ✅ Update image if provided
        if (imageFile != null && !imageFile.isEmpty()) {
            product1.setImageName(imageFile.getOriginalFilename());
            product1.setImageType(imageFile.getContentType());
            product1.setImageData(imageFile.getBytes());
        }

        Product savedProduct = productRepository.save(product1);
        return modelMapper.map(savedProduct, ProductDto.class);

    }
    @Override
    public ProductDto getAllProductid(Long productId) {

        Product  product=productRepository.findById(productId)
                .orElseThrow(()-> new ResourceNotFoundException("product","productId",productId));
        return modelMapper.map(product, ProductDto.class);
    }

    @Override
    public String getProductname(Long productId) {
        Product  product=productRepository.findById(productId)
                .orElseThrow(()-> new ResourceNotFoundException("product","productId",productId));
        return product.getProductName();
    }

    @Override
    public String getProductBrand(Long productId) {
        Product  product=productRepository.findById(productId)
                .orElseThrow(()-> new ResourceNotFoundException("product","productId",productId));
        return product.getBrand();
    }
}

