package com.attestation.project.app.model.dto.request;

import com.attestation.project.app.model.dto.response.CustomerResponse;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.Date;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@ToString

public class MessageRequest {

    @Schema(description = "отправитель")
    private CustomerResponse sender;

    @Schema(description = "текст сообщения")
    private String content;

    @Schema(description = "дата")
    private Date timestamp;
}
