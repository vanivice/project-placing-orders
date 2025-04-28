package com.attestation.project.app.model.dto.request;

import com.attestation.project.app.model.enums.OrderStat;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@ToString

public class OrderCatalogRequest {

    private String description;
    private OrderStat status;
}
