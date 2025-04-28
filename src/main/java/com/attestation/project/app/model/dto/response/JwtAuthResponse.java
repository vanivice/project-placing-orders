package com.attestation.project.app.model.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)

@Schema(description = "Ответ c токеном доступа")
public class JwtAuthResponse {

    @Schema(description = "Токен доступа")
    private String token;

    private String message;
}
