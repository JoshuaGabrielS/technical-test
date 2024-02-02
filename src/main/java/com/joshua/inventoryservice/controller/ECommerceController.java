package com.joshua.inventoryservice.controller;

import com.joshua.inventoryservice.dto.UpdateProductDto;
import com.joshua.inventoryservice.dto.ProductDto;
import com.joshua.inventoryservice.dto.OrderDto;
import com.joshua.inventoryservice.service.InventoryService;
import com.joshua.inventoryservice.service.OrderService;
import com.joshua.inventoryservice.utility.MessageModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/E-Commerce")
public class ECommerceController {
    @Autowired
    private InventoryService inventoryService;
    @Autowired
    private OrderService orderService;

    @PostMapping("/addNewProduct")
    private ResponseEntity<MessageModel> addNewProduct(@RequestBody ProductDto productDto) {
        ResponseEntity responseEntity = inventoryService.addNewProduct(productDto);
        return responseEntity;

    }

    @PostMapping("/updateProductQuantity")
    private ResponseEntity<MessageModel> updateProductQuantity(@RequestBody UpdateProductDto updateProductDto) {
        ResponseEntity responseEntity = inventoryService.updateProductQuantity(updateProductDto);
        return responseEntity;
    }

    @GetMapping("/checkProductAvailability")
    public ResponseEntity checkProductAvailability(@RequestParam UUID idProduct) {
        ResponseEntity responseEntity = inventoryService.checkProductAvailability(idProduct);
        return responseEntity;
    }

    @PostMapping("/orderProduct")
    private ResponseEntity<MessageModel> orderProduct(@RequestBody OrderDto orderDto) {
        ResponseEntity responseEntity = orderService.orderProduct(orderDto);
        return responseEntity;
    }
}
