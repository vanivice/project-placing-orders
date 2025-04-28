package com.attestation.project.app.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public class CommonBackendException extends RuntimeException {
    private final String message;
    private final HttpStatus status;
}

