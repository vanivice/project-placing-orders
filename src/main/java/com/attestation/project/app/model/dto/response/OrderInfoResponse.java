package com.attestation.project.app.model.dto.response;

import com.attestation.project.app.model.dto.request.OrderInfoRequest;
import com.attestation.project.app.model.enums.OrderStat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)

public class OrderInfoResponse extends OrderInfoRequest {

    private String id;
    private String customerName;
    private Long customerId;
    private OrderStat status;
    private String message;
}