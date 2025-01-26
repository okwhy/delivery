package dev.delivery.entities;

import dev.delivery.enums.Status;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.proxy.HibernateProxy;

import java.time.LocalDateTime;
import java.util.Objects;

@Getter
@Setter
@Entity
@Table(name = "orders_activity_entries")
public class OrderActivityEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;
    private LocalDateTime timestamp;
    private String description;
    @Enumerated(EnumType.STRING)
    private Status status;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "preformer")
    private PerformerEntity performer;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "order_id", updatable = false)
    private OrderEntity order;
}