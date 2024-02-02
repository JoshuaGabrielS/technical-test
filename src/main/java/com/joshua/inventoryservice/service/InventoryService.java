package com.joshua.inventoryservice.service;

import com.joshua.inventoryservice.dto.UpdateProductDto;
import com.joshua.inventoryservice.dto.ProductDto;
import com.joshua.inventoryservice.entity.Product;
import com.joshua.inventoryservice.repository.OrderRepository;
import com.joshua.inventoryservice.repository.ProductRepository;
import com.joshua.inventoryservice.utility.MessageModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import javax.transaction.Transactional;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Service
public class InventoryService {
    @Autowired
    ProductRepository productRepository;
    @Autowired
    OrderRepository orderRepository;

    public ResponseEntity<MessageModel> addNewProduct(ProductDto productDto) {
        MessageModel msg = new MessageModel();

        try {
            Product simpan = new Product();
            simpan.setName(productDto.getName());
            simpan.setPrice(productDto.getPrice());
            simpan.setQuantity(productDto.getQuantity());
            simpan.setStore(productDto.getStore());
            productRepository.save(simpan);

            msg.setStatus(true);
            msg.setMessage("Success");
            msg.setData(simpan);
            return ResponseEntity.status(HttpStatus.OK).body(msg);
        } catch (Exception e) {
            e.printStackTrace();
            msg.setStatus(false);
            msg.setMessage(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(msg);
        }
    }

    @Transactional
    public ResponseEntity<MessageModel> updateProductQuantity(UpdateProductDto updateProductDto) {
        MessageModel msg = new MessageModel();

        try {
            productRepository.updateProduct(updateProductDto.getIdProduct(), updateProductDto.getQuantity());

            msg.setStatus(true);
            msg.setMessage("Success");
            return ResponseEntity.status(HttpStatus.OK).body(msg);

        } catch (Exception e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            msg.setStatus(false);
            msg.setMessage(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(msg);
        }
    }

    public ResponseEntity checkProductAvailability(UUID idProduct) {
        Map<String, Object> result = new HashMap<>();
        MessageModel msg = new MessageModel();
        try {
            Optional<Product> data = productRepository.findById(idProduct);
            if (!data.isPresent()) {
                msg.setStatus(false);
                msg.setMessage("data tidak ditemukan");
                msg.setData(null);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(msg);
            } else {
                msg.setStatus(true);
                msg.setMessage("Success");
                result.put("quantity", data.get().getQuantity());
                msg.setData(result);
                return ResponseEntity.status(HttpStatus.OK).body(msg);
            }

        } catch (Exception e) {
            msg.setStatus(false);
            msg.setMessage(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(msg);

        }
    }


}
