package dev.delivery.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "orders")
public class OrderEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;
    private String comment;


    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "address")
    private AddressEntity address;

    @OneToMany(mappedBy = "orderEntity", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<ProductItemEntity> products = new LinkedHashSet<>();

    @ManyToOne
    @JoinColumn(name = "box")
    private BoxEntity box;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<OrderActivityEntity> orderActivityEntities = new LinkedHashSet<>();

    @ManyToOne
    @JoinColumn(name = "client")
    private ClientEntity client;

}