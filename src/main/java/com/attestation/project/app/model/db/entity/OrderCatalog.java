package com.attestation.project.app.model.db.entity;

import com.attestation.project.app.model.enums.OrderStat;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "catalogs")

public class OrderCatalog {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "description")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private OrderStat status;

    @JsonBackReference
    @OneToMany(mappedBy = "catalog", cascade = CascadeType.ALL)
    private List<Order> orderList;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JsonBackReference(value = "orders-executors")
    private Executor executor;
}
