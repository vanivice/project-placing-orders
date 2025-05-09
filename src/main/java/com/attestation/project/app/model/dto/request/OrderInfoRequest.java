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

    @Schema(description = "заголовок")
    private String title;

    @Schema(description = "требования")
    private String requirements;

    @Schema(description = "сроки")
    private String deadlines;

    @Schema(description = "стоимость")
    private String cost;

    @Schema(description = "дата публикации")
    private LocalDateTime createDate;
}
