package com.attestation.project.app.model.dto.response;

import com.attestation.project.app.model.dto.request.ExecutorRequest;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor

public class ExecutorResponse extends ExecutorRequest {

    private Long id;
}
