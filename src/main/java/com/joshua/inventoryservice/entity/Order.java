package com.joshua.inventoryservice.entity;

import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.util.UUID;
@Setter
@Getter
@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "tb_order", schema = "inventory")
public class Order implements Serializable {

    @Id
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "uuid2")
    @Column(name = "id_order", nullable = false)
    private UUID idOrder;

    @Column(name = "id_user", nullable = false)
    private UUID idUser;

    @Column(name = "id_product", nullable = false)
    private UUID idProduct;

    @Column(name = "quantity", nullable = false)
    private Long quantity;
}
