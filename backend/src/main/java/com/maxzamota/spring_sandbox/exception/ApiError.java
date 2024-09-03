package com.maxzamota.spring_sandbox.exception;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class ApiError {

    private HttpStatus httpStatus;
    private LocalDateTime timestamp;
    private String errorMessage;
    private String debugMessage;
    private List<ApiSubError> subErrors;

    private ApiError() {
        timestamp = LocalDateTime.now();
    }

    public ApiError(HttpStatus httpStatus, Throwable exception) {
        this();
        this.httpStatus = httpStatus;
        this.errorMessage = "Something went wrong";
        this.debugMessage = exception.getLocalizedMessage();
    }

    public ApiError(HttpStatus httpStatus, String message, Throwable exception) {
        this();
        this.httpStatus = httpStatus;
        this.errorMessage = message;
        this.debugMessage = exception.getLocalizedMessage();
    }
}
