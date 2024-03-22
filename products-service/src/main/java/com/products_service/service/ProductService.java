package com.products_service.service;

import java.util.List;
import com.products_service.dto.ProductRequest;
import com.products_service.dto.ProductResponse;
import com.products_service.models.Product;

public interface ProductService {
    void addProduct(ProductRequest productRequest)throws Exception;
    List<ProductResponse> getAllProducts();
    ProductResponse getProductBySku(String sku)throws Exception;
}
