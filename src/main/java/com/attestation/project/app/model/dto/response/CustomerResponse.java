package com.attestation.project.app.model.dto.response;

import com.attestation.project.app.model.dto.request.CustomerRequest;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)

public class CustomerResponse extends CustomerRequest {

    private Long id;
    private String message;
}