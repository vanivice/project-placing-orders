package com.attestation.project.app.model.db.entity;

import com.attestation.project.app.model.enums.OrderStat;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "orders")

public class Order {

    @Id
    @Column(name = "id")
    private String id = UUID.randomUUID().toString();

    @Column(name = "title")
    private String title;

    @Column(name = "requirements")
    private String requirements;

    @Column(name = "deadlines")
    private String deadlines;

    @Column(name = "create_date")
    private LocalDateTime createDate;

    @Column(name = "cost")
    private String cost;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private OrderStat status;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "catalog_id")
    private OrderCatalog catalog;
}
