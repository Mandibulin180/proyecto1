package com.inventary_service.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.inventary_service.models.Inventory;
import com.inventary_service.models.dto.BaseResponse;
import com.inventary_service.models.dto.OrderItemsRequest;
import com.inventary_service.repository.InventoryRepository;

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

        List<Inventory> invenrotyList = inventoryRepository.findBySkuIn(skus);

        orderItems.forEach( orderitem -> {
            var inventory = invenrotyList.stream().filter(value -> value.getSku().equals(orderitem.getSku())).findFirst();
            if(inventory.isEmpty()){
                errorList.add("Product with sku " + orderitem.getSku() + " does not exist");
            }else if(inventory.get().getQuantity() < orderitem.getQuantity()){
                errorList.add("Product with sku " + orderitem.getSku() + "has insufficient quantity");
            }
        });

        return errorList.size() > 0 ? new BaseResponse(errorList.toArray(new String[0])) : new BaseResponse(null);
        
    }

    
}
