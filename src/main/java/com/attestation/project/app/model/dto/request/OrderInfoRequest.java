package com.attestation.project.app.model.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@ToString

public class OrderInfoRequest {

    @NonNull
    @Schema(description = "заголовок")
    private String title;

    @NonNull
    @Schema(description = "требования")
    private String requirements;

    @NonNull
    @Schema(description = "сроки")
    private String deadlines;

    @NonNull
    @Schema(description = "стоимость")
    private String cost;

    private LocalDateTime createDate;
}
