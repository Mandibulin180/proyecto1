package com.orders_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.orders_service.models.Order;

@Repository
public interface OrderRepository extends JpaRepository<Order,Long> {
    
}
