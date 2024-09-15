package com.dailycodework.dreamshops.controller;

import com.dailycodework.dreamshops.dto.ProductDto;
import com.dailycodework.dreamshops.exception.AlreadyExistsException;
import com.dailycodework.dreamshops.exception.ResourceNotFoundException;
import com.dailycodework.dreamshops.model.Product;
import com.dailycodework.dreamshops.request.AddProductRequest;
import com.dailycodework.dreamshops.request.ProductUpdateRequest;
import com.dailycodework.dreamshops.response.ApiResponse;
import com.dailycodework.dreamshops.service.product.IProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("${api.prefix}/products")

public class ProductController {

    private final IProductService productService;

    @GetMapping("/all")
    public ResponseEntity<ApiResponse>getAllProduct(){
        List<Product>products=productService.getAllProducts();
        List<ProductDto>convertedProducts=productService.getConvertedProducts(products);
return ResponseEntity.ok(new ApiResponse("Success",convertedProducts));
    }

@GetMapping("product/{productId}/product")
    public ResponseEntity<ApiResponse>getProductById(@PathVariable Long productId){
        try {
            Product product =productService.getProductById(productId);
           ProductDto productDto=productService.convertToDto(product);

            return ResponseEntity.ok(new ApiResponse("Success",productDto));
        } catch (ResourceNotFoundException e) {

            return  ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(),null));

        }
    }

@PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/add")
    public ResponseEntity<ApiResponse>addProduct(@RequestBody AddProductRequest product){
        try {
            Product theProduct=productService.addProduct(product);
            ProductDto productDto=productService.convertToDto(theProduct);
            return ResponseEntity.ok(new ApiResponse("Add product success",productDto));
        } catch (AlreadyExistsException e) {
            return ResponseEntity.status(CONFLICT).body(new ApiResponse(e.getMessage(),null));
        }
    }
    @PreAuthorize("hasRole('ROLE_ADMIN')")
@PutMapping("/product/{productId}/update")
    public ResponseEntity<ApiResponse>updateProduct(@RequestBody ProductUpdateRequest request,@PathVariable Long productId){
        try {
            Product theProduct= productService.updateProduct(request,productId);
            ProductDto productDto=productService.convertToDto(theProduct);
            return ResponseEntity.ok(new ApiResponse("Update product success!",productDto));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(),null));
        }
    }
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/product/{productId}/delete")
    public ResponseEntity<ApiResponse>deleteProduct(@PathVariable Long productId){
        try {
            productService.deleteProductById(productId);
            return ResponseEntity.ok(new ApiResponse("Delete product success",productId));
        } catch (ResourceNotFoundException e) {
          return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(),null));
        }
    }
@GetMapping("/products/by/brand-and-name")
    public ResponseEntity<ApiResponse>getProductByBrandAndName(@RequestParam String brandName, @RequestParam String productName) {

        try {
            List<Product> products = productService.getProductByBrandAndName(brandName, productName);
            List<ProductDto>convertedProducts=productService.getConvertedProducts(products);

            if (products.isEmpty()) {
                return ResponseEntity.status(NOT_FOUND).body(new ApiResponse("No products Found", null));
            }

            return ResponseEntity.ok(new ApiResponse("success", convertedProducts));
        }
        catch (Exception e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(),null));
        }
    }
    @GetMapping("/products/by/category-and-brand")
    public ResponseEntity<ApiResponse>getProductByCategoryAndBrand(@RequestParam String category, @RequestParam String brand) {
        try {
            List<Product> products = productService.getProductByBrandAndName(category, brand);
            if (products.isEmpty()) {
                return ResponseEntity.status(NOT_FOUND).body(new ApiResponse("No products Found", null));
            }
            List<ProductDto>convertedProducts=productService.getConvertedProducts(products);
            return ResponseEntity.ok(new ApiResponse("success", products));
        }
        catch (Exception e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(),null));
        }
    }
    @GetMapping("/products/{name}/products")
    public ResponseEntity<ApiResponse>getProductByName(@PathVariable String name) {
        try {
            List<Product> products = productService.getProductByName(name);
            if (products.isEmpty()) {
                return ResponseEntity.status(NOT_FOUND).body(new ApiResponse("No products Found", null));
            }
            List<ProductDto>convertedProducts=productService.getConvertedProducts(products);
            return ResponseEntity.ok(new ApiResponse("success", products));
        }
        catch (Exception e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse("error",e.getMessage()));
        }
    }
    @GetMapping("/products/by-brand")
    public ResponseEntity<ApiResponse>getProductByBrand(@RequestParam String brand) {
        try {
            List<Product> products = productService.getProductByBrand(brand);
            if (products.isEmpty()) {
                return ResponseEntity.status(NOT_FOUND).body(new ApiResponse("No products Found", null));
            }
            List<ProductDto>convertedProducts=productService.getConvertedProducts(products);
            return ResponseEntity.ok(new ApiResponse("success", products));
        }
        catch (Exception e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse("error",e.getMessage()));
        }
    }

    @GetMapping("/products/{category}/all/products")
    public ResponseEntity<ApiResponse>findProductByCategory(@PathVariable String category) {
        try {
            List<Product> products = productService.getProductByCategory(category);
            if (products.isEmpty()) {
                return ResponseEntity.status(NOT_FOUND).body(new ApiResponse("No products Found", null));
            }
            List<ProductDto>convertedProducts=productService.getConvertedProducts(products);
            return ResponseEntity.ok(new ApiResponse("success", products));
        }
        catch (Exception e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse("error",e.getMessage()));
        }
    }

    @GetMapping("/products/count/by-brand/and-name")
    public ResponseEntity<ApiResponse>countProductByBrandAndName(@RequestParam String brand,@RequestParam String name) {
        try {
            var productCount  = productService.countProductByBrandAndName(brand,name);
            return ResponseEntity.ok(new ApiResponse("Product Count!", productCount));
        }
        catch (Exception e) {
            return ResponseEntity.ok(new ApiResponse(e.getMessage(),null));
        }
    }

    }

