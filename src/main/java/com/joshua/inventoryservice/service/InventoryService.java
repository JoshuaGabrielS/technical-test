package com.joshua.inventoryservice.service;

import com.joshua.inventoryservice.dto.UpdateProductDto;
import com.joshua.inventoryservice.dto.ProductDto;
import com.joshua.inventoryservice.entity.Product;
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

    public ResponseEntity<MessageModel> addNewProduct(ProductDto productDto) {
        // Membuat objek untuk menyimpan respon
        MessageModel msg = new MessageModel();

        try {
            // Membuat objek produk baru
            Product simpan = new Product();

            // Mengisi data produk baru dengan informasi dari DTO
            simpan.setName(productDto.getName());
            simpan.setPrice(productDto.getPrice());
            simpan.setQuantity(productDto.getQuantity());
            simpan.setStore(productDto.getStore());

            // Menyimpan produk baru ke dalam repositori
            productRepository.save(simpan);

            // Mengatur status true dan pesan sukses pada respon
            msg.setStatus(true);
            msg.setMessage("Success");
            msg.setData(simpan);

            // Mengembalikan respon dengan status OK dan pesan sukses
            return ResponseEntity.status(HttpStatus.OK).body(msg);
        } catch (Exception e) {
            // Menangani exception dengan mencetak stack trace dan mengatur status false pada respon
            e.printStackTrace();
            msg.setStatus(false);
            msg.setMessage(e.getMessage());

            // Mengembalikan respon dengan status INTERNAL_SERVER_ERROR dan pesan exception
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(msg);
        }
    }

    @Transactional
    public ResponseEntity<MessageModel> updateProductQuantity(UpdateProductDto updateProductDto) {
        // Membuat objek untuk menyimpan respon
        MessageModel msg = new MessageModel();

        try {
            // Memanggil metode repository untuk melakukan pembaruan kuantitas produk
            productRepository.updateProduct(updateProductDto.getIdProduct(), updateProductDto.getQuantity());

            // Jika pembaruan berhasil, set status true dan kembalikan respon dengan status OK
            msg.setStatus(true);
            msg.setMessage("Success");
            return ResponseEntity.status(HttpStatus.OK).body(msg);

        } catch (Exception e) {
            // Jika terjadi exception, melakukan rollback transaksi
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();

            // Set status false dan kembalikan respon dengan status INTERNAL_SERVER_ERROR dan pesan exception
            msg.setStatus(false);
            msg.setMessage(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(msg);
        }
    }

    public ResponseEntity checkProductAvailability(UUID idProduct) {
        // Membuat objek untuk menyimpan respon
        Map<String, Object> result = new HashMap<>();
        MessageModel msg = new MessageModel();

        try {
            // Mengambil data produk berdasarkan ID menggunakan repository
            Optional<Product> data = productRepository.findById(idProduct);

            // Memeriksa apakah produk ditemukan
            if (!data.isPresent()) {
                // Jika produk tidak ditemukan, mengembalikan respon dengan status NOT_FOUND
                msg.setStatus(false);
                msg.setMessage("Data tidak ditemukan");
                msg.setData(null);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(msg);
            } else {
                // Jika produk ditemukan, mengembalikan respon dengan status OK dan informasi ketersediaan produk
                msg.setStatus(true);
                msg.setMessage("Success");
                result.put("quantity", data.get().getQuantity());
                msg.setData(result);
                return ResponseEntity.status(HttpStatus.OK).body(msg);
            }
        } catch (Exception e) {
            // Menangani exception dan mengembalikan respon dengan status INTERNAL_SERVER_ERROR
            msg.setStatus(false);
            msg.setMessage(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(msg);
        }
    }

}
