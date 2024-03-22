package com.inventary_service.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.inventary_service.models.dto.BaseResponse;
import com.inventary_service.models.dto.OrderItemsRequest;
import com.inventary_service.models.dto.Product;
import com.inventary_service.service.InventoryService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/inventory")
@RequiredArgsConstructor
public class InventoryController {

    private final InventoryService inventoryService;

    @GetMapping("{sku}")
    @ResponseStatus(HttpStatus.OK)
    public Boolean isInStock(@PathVariable("sku") String sku){
        return inventoryService.isInStock(sku);
    }

    @PostMapping("/in-stock")
    @ResponseStatus(HttpStatus.OK)
    public BaseResponse areInStock(@RequestBody List<OrderItemsRequest> orderItems){
        return inventoryService.areInStock(orderItems);
    }

    // TODO hacer una request que llame al controlador de productos para saber si hay exiten un sku y agregarle una catidad

    @PostMapping()
    public ResponseEntity<Object> createInventory(@RequestBody Product product){
        return new ResponseEntity<>(inventoryService.createInventory(product),HttpStatus.CREATED);
    }
    

    
}
