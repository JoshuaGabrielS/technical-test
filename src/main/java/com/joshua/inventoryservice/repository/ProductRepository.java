package com.joshua.inventoryservice.repository;

import com.joshua.inventoryservice.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ProductRepository extends JpaRepository<Product, UUID> {
    @Modifying
    @Query(value = "UPDATE inventory.tb_product \n" +
            "SET quantity= :quantity \n" +
            "WHERE id_product=:idProduct", nativeQuery = true)
    void updateProduct(@Param("idProduct") UUID idProduct, @Param("quantity") Long quantity);
}
