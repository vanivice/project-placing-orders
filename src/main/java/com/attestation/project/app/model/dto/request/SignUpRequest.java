package com.attestation.project.app.model.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Запрос на регистрацию")
public class SignUpRequest {

    @Schema(description = "Логин пользователя")
    @Size(min = 5, max = 50, message = "Логин должен содержать от 5 до 50 символов")
    @NotBlank(message = "Логин не может быть пустыми")
    private String username;

    @Schema(description = "Имя пользователя", example = "Антон")
    @Size(min = 1, max = 50, message = "Имя пользователя должно содержать от 5 до 50 символов")
    @NotBlank(message = "Имя пользователя не может быть пустыми")
    private String firstname;

    @Schema(description = "Адрес электронной почты")
    @Size(min = 5, max = 255, message = "Адрес электронной почты должен содержать от 5 до 255 символов")
    @NotBlank(message = "Адрес электронной почты не может быть пустыми")
    private String email;

    @Schema(description = "Пароль")
    @Size(max = 255, message = "Длина пароля должна быть не более 255 символов")
    private String password;
}