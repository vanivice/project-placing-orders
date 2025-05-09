package com.attestation.project.app.model.dto.response;

import com.attestation.project.app.model.dto.request.ReviewInfoRequest;
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

public class ReviewInfoResponse extends ReviewInfoRequest {

    private Long id;
    private String message;
}
