package com.products_service.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.products_service.dto.ProductRequest;
import com.products_service.dto.ProductResponse;
import com.products_service.models.Product;
import com.products_service.repository.InventoryRepository;
import com.products_service.repository.ProductRepository;
import com.products_service.models.Inventory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final InventoryRepository inventoryRepository;

    public void addProduct(ProductRequest productRequest)throws Exception{
        var product = Product.builder()
        .sku(skuValid(productRequest.getSku()))
        .name(productRequest.getName())
        .description(productRequest.getDescription())
        .price(productRequest.getPrice())
        .status(productRequest.getStatus())
        .build();

        var inventory = Inventory.builder()
        .product(product)
        .build();
        
        inventoryRepository.save(inventory);
        productRepository.save(product);
    }

    public List<ProductResponse> getAllProducts(){
        var products = productRepository.findAll();
        return products.stream().map(this::mapToProductResponse).toList();
    }

    private String skuValid(String sku)throws Exception{
        Optional<Product> optionalSku = productRepository.findBySku(sku);

        if(optionalSku.isEmpty()){
            return sku;
        }else{
            throw new Exception();
        }
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
