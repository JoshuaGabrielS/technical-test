package com.joshua.inventoryservice.service;

import com.joshua.inventoryservice.dto.OrderDto;
import com.joshua.inventoryservice.entity.Order;
import com.joshua.inventoryservice.entity.Product;
import com.joshua.inventoryservice.repository.OrderRepository;
import com.joshua.inventoryservice.repository.ProductRepository;
import com.joshua.inventoryservice.utility.MessageModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;


@Service
public class OrderService {
    @Autowired
    OrderRepository orderRepository;
    @Autowired
    ProductRepository productRepository;

    public ResponseEntity orderProduct(OrderDto orderDto) {
        MessageModel msg = new MessageModel();
        try {
            Optional<Product> data = productRepository.findById(orderDto.getIdProduct());
            if (!data.isPresent()) {
                msg.setStatus(true);
                msg.setMessage("data tidak ditemukan");
                msg.setData(null);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(msg);
            } else {
                Product product = data.get();
                if (product.getQuantity() >= orderDto.getQuantity()) {
                    product.setQuantity(product.getQuantity() - orderDto.getQuantity());
                    Order order = new Order();
                    order.setIdProduct(orderDto.getIdProduct());
                    order.setIdUser(orderDto.getIdUser());
                    order.setQuantity(orderDto.getQuantity());
                    orderRepository.save(order);
                    productRepository.save(product);

                    msg.setStatus(true);
                    msg.setMessage("Success");
                    msg.setData(order);
                    return ResponseEntity.ok().body(msg);
                } else {
                    msg.setStatus(false);
                    msg.setMessage("Stock Tidak Mencukupi");
                    return ResponseEntity.status(HttpStatus.OK).body(msg);
                }
            }

        } catch (Exception e) {
            msg.setStatus(false);
            msg.setMessage(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(msg);

        }
    }


}
