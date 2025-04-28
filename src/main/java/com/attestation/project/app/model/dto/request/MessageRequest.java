package com.attestation.project.app.model.dto.request;

import com.attestation.project.app.model.dto.response.CustomerResponse;
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

    private CustomerResponse sender;
    private String content;
    private Date timestamp;
}
