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
        // Membuat objek untuk menyimpan pesan respon
        MessageModel msg = new MessageModel();

        try {
            // Mengambil data produk berdasarkan ID menggunakan repository
            Optional<Product> data = productRepository.findById(orderDto.getIdProduct());

            // Memeriksa apakah produk ditemukan
            if (!data.isPresent()) {
                // Jika produk tidak ditemukan, mengembalikan respon dengan status NOT_FOUND
                msg.setStatus(true);
                msg.setMessage("Data tidak ditemukan");
                msg.setData(null);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(msg);
            } else {
                // Jika produk ditemukan, mengurangi stok jika cukup
                Product product = data.get();
                if (product.getQuantity() >= orderDto.getQuantity()) {
                    // Mengurangi stok produk
                    product.setQuantity(product.getQuantity() - orderDto.getQuantity());

                    // Membuat objek pesanan dan mengisi data pesanan
                    Order order = new Order();
                    order.setIdProduct(orderDto.getIdProduct());
                    order.setIdUser(orderDto.getIdUser());
                    order.setQuantity(orderDto.getQuantity());

                    // Menyimpan pesanan dan memperbarui stok produk
                    orderRepository.save(order);
                    productRepository.save(product);

                    // Mengembalikan respon sukses dengan data pesanan
                    msg.setStatus(true);
                    msg.setMessage("Success");
                    msg.setData(order);
                    return ResponseEntity.ok().body(msg);
                } else {
                    // Jika stok tidak mencukupi, mengembalikan respon dengan status OK
                    msg.setStatus(false);
                    msg.setMessage("Stok Tidak Mencukupi");
                    return ResponseEntity.status(HttpStatus.OK).body(msg);
                }
            }
        } catch (Exception e) {
            // Menangani exception dan mengembalikan respon dengan status INTERNAL_SERVER_ERROR
            msg.setStatus(false);
            msg.setMessage(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(msg);
        }
    }



}
