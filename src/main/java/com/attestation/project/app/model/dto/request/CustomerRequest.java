package com.attestation.project.app.model.dto.request;

import com.attestation.project.app.model.enums.Role;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@ToString

public class CustomerRequest {

    @Schema(description = "эл. почта")
    private String email;

    @Schema(description = "логин", example = "art_master")
    private String username;

    @Schema(description = "имя", example = "Иван")
    private String firstname;

    @Schema(description = "роль")
    private Role role;
}
