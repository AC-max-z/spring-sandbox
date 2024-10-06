package com.maxzamota.spring_sandbox.exception;

import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.MappingException;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.password.CompromisedPasswordException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Arrays;
import java.util.List;

@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
@Slf4j
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    private ResponseEntity<Object> buildResponseEntity(ApiError apiError) {
        return new ResponseEntity<>(apiError, apiError.getHttpStatus());
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(
            HttpMessageNotReadableException ex,
            HttpHeaders headers,
            HttpStatusCode status,
            WebRequest request
    ) {
        String error = "Malformed JSON request";
        return buildResponseEntity(new ApiError(HttpStatus.BAD_REQUEST, error, ex));
    }

    @ExceptionHandler(CompromisedPasswordException.class)
    public String handleCompromisedPasswordException(CompromisedPasswordException e,
                                                     RedirectAttributes attributes) {
        attributes.addFlashAttribute("error", e.getMessage());
        return "redirect:/reset-password";
    }

    @ExceptionHandler(EntityNotFoundException.class)
    protected ResponseEntity<Object> handleEntityNotFound(EntityNotFoundException ex) {
        ApiError apiError = new ApiError(HttpStatus.NOT_FOUND, ex);
        return buildResponseEntity(apiError);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Object> handleDataIntegrityViolation(Exception ex) {
        ApiError apiError = new ApiError(HttpStatus.INTERNAL_SERVER_ERROR, ex);
        apiError.setDebugMessage("Please contact application administrator for more details");
        return buildResponseEntity(apiError);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Object> handleResourceNotFound(Exception ex) {
        return buildResponseEntity(new ApiError(HttpStatus.NOT_FOUND, ex));
    }

    @ExceptionHandler(DuplicateResourceException.class)
    public ResponseEntity<Object> handleDuplicateResource(Exception ex) {
        return buildResponseEntity(new ApiError(HttpStatus.CONFLICT, ex));
    }

    @ExceptionHandler(MappingException.class)
    public ResponseEntity<Object> handleMappingError(Exception ex) {
        ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, ex);
        ApiValidationError validationError = new ApiValidationError(
                null, ex.getCause().getLocalizedMessage());
        apiError.setSubErrors(List.of(validationError));
        return buildResponseEntity(apiError);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<Object> handleAccessDeniedException(Exception e) {
        var apiError = new ApiError(HttpStatus.FORBIDDEN, e);
        return buildResponseEntity(apiError);
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleEverythingElse(Exception e) {
        logger.debug("Exception type: " + e.getClass().getSimpleName());
        logger.error(e.getMessage());
        logger.debug("Stacktrace:\n" + Arrays.toString(e.getStackTrace()));
        var apiError = new ApiError(HttpStatus.INTERNAL_SERVER_ERROR, e);
        apiError.setDebugMessage("Please contact application administrator for more details");
        return buildResponseEntity(apiError);
    }
}
