package com.products_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.products_service.models.Inventory;

@Repository
public interface InventoryRepository extends JpaRepository<Inventory,Long> {
    
}
