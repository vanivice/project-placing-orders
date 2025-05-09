package com.attestation.project.app.model.db.entity;

import com.attestation.project.app.model.enums.Estimation;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "reviews")

public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String orderId;

    @Column
    private String textReview;

    @Enumerated(EnumType.STRING)
    @Column
    private Estimation estimation;

    @ManyToOne
    private Customer estimator;

    @ManyToOne
    private Executor executor;
}
