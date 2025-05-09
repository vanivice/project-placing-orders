package com.attestation.project.app.model.db.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "messages")

public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Customer sender;

    @ManyToOne
    private Customer receiver;

    private String content;

    @Temporal(TemporalType.TIMESTAMP)
    private Date timestamp;
}
