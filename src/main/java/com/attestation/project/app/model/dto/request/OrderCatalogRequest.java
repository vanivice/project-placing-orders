package com.attestation.project.app.model.dto.request;

import com.attestation.project.app.model.enums.OrderStat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@ToString

public class OrderCatalogRequest {

    @Schema(description = "описание заказа")
    private String description;

    @Schema(description = "статус заказа")
    private OrderStat status;
}
