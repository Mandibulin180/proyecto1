package com.products_service.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.products_service.dto.ProductRequest;
import com.products_service.dto.ProductResponse;
import com.products_service.models.Product;
import com.products_service.repository.ProductRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductService {

    private final ProductRepository productRepository;

    public void addProduct(ProductRequest productRequest){
        var product = Product.builder()
        .sku(productRequest.getSku())
        .name(productRequest.getName())
        .description(productRequest.getDescription())
        .price(productRequest.getPrice())
        .status(productRequest.getStatus())
        .build();

        productRepository.save(product);

        log.info("Producto added: {}", product);
    }

    public List<ProductResponse> getAllProducts(){
        var products = productRepository.findAll();
        return products.stream().map(this::mapToProductResponse).toList();
    }

    private ProductResponse mapToProductResponse(Product product){
        return  ProductResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .sku(product.getSku())
                .description(product.getDescription())
                .price(product.getPrice())
                .status(product.getStatus())
                .build();
    }
    
}