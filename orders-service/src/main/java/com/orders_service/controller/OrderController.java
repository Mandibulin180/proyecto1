package com.orders_service.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.orders_service.dto.OrderRequest;
import com.orders_service.dto.OrderResponse;
import com.orders_service.service.OrderService;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;

@RestController
@RequestMapping("/api/order")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;
    
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('admin')")
    @CircuitBreaker(name ="orders-service",fallbackMethod = "placeOrderFallback")
    public ResponseEntity<OrderResponse> makeOrder(@RequestBody OrderRequest orderRequest){
        var order = this.orderService.placeOrder(orderRequest);
        return ResponseEntity.ok(order);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('user')")
    public List<OrderResponse> getAllOrders(){
        return this.orderService.getAllOrders();
    }

    private ResponseEntity<OrderResponse> placeOrderFallback(OrderRequest orderRequest,Throwable throwable){
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).build();
    }
    
}
