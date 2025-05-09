package com.attestation.project.app.model.dto.request;

import com.attestation.project.app.model.dto.response.CustomerResponse;
import com.attestation.project.app.model.enums.Estimation;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@ToString

public class ReviewInfoRequest {

    @Schema(description = "оценщик")
    private CustomerResponse estimator;

    @Schema(description = "номер заказа")
    private String orderId;

    @Schema(description = "текст отзыва")
    private String textReview;

    @Schema(description = "оценка")
    private Estimation estimation;
}
