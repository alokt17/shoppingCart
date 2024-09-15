package com.dailycodework.dreamshops.service.product;

import com.dailycodework.dreamshops.dto.ProductDto;
import com.dailycodework.dreamshops.model.Product;
import com.dailycodework.dreamshops.request.AddProductRequest;
import com.dailycodework.dreamshops.request.ProductUpdateRequest;

import java.util.List;

public interface IProductService {

    Product addProduct(AddProductRequest product);

    Product getProductById(Long id);
    void deleteProductById(Long id);
    Product updateProduct(ProductUpdateRequest product, Long productId);
    List<Product>getAllProducts();
    List<Product>getProductByCategory(String category);
    List<Product>getProductByBrand(String brand);
    List<Product>getProductByCategoryAndBrand(String category, String brand);
    List<Product>getProductByName(String name);
    List<Product>getProductByBrandAndName(String brand, String name);

    Long countProductByBrandAndName(String brand, String name);

    List<ProductDto> getConvertedProducts(List<Product> products);

    ProductDto convertToDto(Product product);
}
