package com.attestation.project.app.model.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ExecutorRequest {

    @Schema(description = "эл. почта")
    private String email;

    @Schema(description = "имя")
    private String firstname;

    @Schema(description = "информация о себе")
    private String description;
}
