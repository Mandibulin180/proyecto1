package com.inventary_service.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.inventary_service.models.Inventory;
import com.inventary_service.models.dto.BaseResponse;
import com.inventary_service.models.dto.OrderItemsRequest;
import com.inventary_service.models.dto.Product;
import com.inventary_service.repository.InventoryRepository;

import jakarta.ws.rs.NotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class InventoryService {

    private final InventoryRepository inventoryRepository;

    public Boolean isInStock(String sku) {
        var inventory = inventoryRepository.findBySku(sku);

        return inventory.filter(value -> value.getQuantity() > 0).isPresent();
    }

    public BaseResponse areInStock(List<OrderItemsRequest> orderItems) {

        var errorList = new ArrayList<String>();

        List<String> skus = orderItems.stream().map(OrderItemsRequest::getSku).toList();

        List<Inventory> inventoryList = inventoryRepository.findBySkuIn(skus);

        orderItems.forEach(orderItem -> {
            var inventory = inventoryList.stream().filter(value -> value.getSku().equals(orderItem.getSku())).findFirst();
            if (inventory.isEmpty()) {
                errorList.add("Product with sku " + orderItem.getSku() + " does not exist");
            } else if (inventory.get().getQuantity() < orderItem.getQuantity()) {
                errorList.add("Product with sku " + orderItem.getSku() + " has insufficient quantity");
            }
        });

        return errorList.size() > 0 ? new BaseResponse(errorList.toArray(new String[0])) : new BaseResponse(null);
    }

    public Inventory createInventory(Product product){
        Inventory inventory = Inventory.builder()
            .sku(product.getSku())
            .quantity(1L)
            .build();
            
        return inventoryRepository.save(inventory);
    }

    // TODO Inventario va a agregar y quitar cantidades

    public void updateInventory(String sku,int quantity,String operation) throws Exception{
            Optional<Inventory> optionaInventory = inventoryRepository.findBySku(sku);

            if(optionaInventory.isEmpty()){
                throw new NotFoundException();
            }else{
                Inventory inventory = optionaInventory.get();
                switch (operation) {
                    case "agregar" :
                        inventory.setQuantity(inventory.getQuantity() + quantity);
                        inventoryRepository.save(inventory);
                        break;
                
                    case "sustraer" :
                        inventory.setQuantity(inventory.getQuantity() - quantity);
                        inventoryRepository.save(inventory);
                        break;
                }
            }
    }
    
}
