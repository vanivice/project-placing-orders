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

    @NonNull
    @Schema(description = "эл. почта")
    private String email;

    @NonNull
    @Schema(description = "имя")
    private String firstname;

    @NonNull
    @Schema(description = "информация о себе")
    private String description;
}
