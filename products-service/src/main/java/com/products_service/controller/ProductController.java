package com.products_service.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.products_service.dto.ProductRequest;
import com.products_service.dto.ProductResponse;
import com.products_service.service.ProductService;

@RestController
@RequestMapping("/api/product")

public class ProductController {

    @Autowired
    private ProductService productService;

    @PostMapping
    @PreAuthorize("hasRole('admin')")
    public ResponseEntity<String> addProduct(@RequestBody ProductRequest productRequest)throws Exception{
        try {
            productService.addProduct(productRequest);
            return new ResponseEntity<>(HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>("El sku debe ser unico",HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('ROLE_USER')")
    public List<ProductResponse> getAllProducts(){
        return this.productService.getAllProducts();
    }
    
}
