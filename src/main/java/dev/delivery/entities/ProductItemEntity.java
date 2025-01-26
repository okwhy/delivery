package dev.delivery.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "product_items")
public class ProductItemEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;

    private Integer quantity;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "product")
    private ProductEntity product;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "order_ref")
    private OrderEntity orderEntity;


}
