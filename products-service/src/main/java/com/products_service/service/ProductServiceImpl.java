package com.products_service.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import reactor.core.publisher.Mono;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;
import com.products_service.dto.Inventory;
import com.products_service.dto.ProductRequest;
import com.products_service.dto.ProductResponse;
import com.products_service.models.Product;
import com.products_service.repository.ProductRepository;
import io.micrometer.observation.Observation;
import io.micrometer.observation.ObservationRegistry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import java.lang.Exception;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final WebClient webClient;
    private final ObservationRegistry observationRegistry;

    @Override
    public void addProduct(ProductRequest productRequest)throws Exception{
        var product = Product.builder()
        .sku(skuValid(productRequest.getSku()))
        .name(productRequest.getName())
        .description(productRequest.getDescription())
        .price(productRequest.getPrice())
        .status(productRequest.getStatus())
        .build();

        Map<String,String> data = new HashMap<String,String>();
        data.put("sku", product.getSku());

        webClient.post()
            .uri("lb://inventory-service/api/inventory")
            .bodyValue(data);
            .retrieve()
            .bodyToMono(String.class)
            .block();
         
        productRepository.save(product);
    }

    @Override
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

    @Override
    public ProductResponse getProductBySku(String sku)throws Exception{
        Optional<Product> producto = productRepository.findBySku(sku);

        if(producto.isEmpty()){
            throw new Exception();
        } else{
            return mapToProductResponse(producto.get());
        }
    }

    private ProductResponse mapToProductResponse(Product product){
        return  ProductResponse.builder()
                .name(product.getName())
                .sku(product.getSku())
                .description(product.getDescription())
                .price(product.getPrice())
                .status(product.getStatus() ? "Esta disponible" : "No esta disponible")
                .build();
    }
}
