package com.products_service.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import com.products_service.dto.ProductRequest;
import com.products_service.dto.ProductResponse;
import com.products_service.service.ProductServiceImpl;

@RestController
@RequestMapping("/api/product")

public class ProductController {

    @Autowired
    private ProductServiceImpl productService;

    @PostMapping
    @PreAuthorize("hasRole('admin')")
    public ResponseEntity<Object> addProduct(@RequestBody ProductRequest productRequest)throws Exception{
        try {
            productService.addProduct(productRequest);
            return new ResponseEntity<>(HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(),HttpStatus.BAD_GATEWAY);
        }
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('ROLE_USER')")
    public List<ProductResponse> getAllProducts(){
        return this.productService.getAllProducts();
    }

    @GetMapping("/{sku}")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<Object> getProuctBySku(@PathVariable String sku)throws Exception{
        try {
            return new ResponseEntity<>(productService.getProductBySku(sku),HttpStatus.OK);
        } catch (Exception e){
            return new ResponseEntity<>("No hay ningun producto con ese Sku",HttpStatus.NOT_FOUND);
        }
    }
    
}
