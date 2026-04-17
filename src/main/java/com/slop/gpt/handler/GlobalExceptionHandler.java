package com.slop.gpt.handler;

import com.slop.gpt.dto.ErrorResponseDto;
import com.slop.gpt.exception.BadRequestException;
import com.slop.gpt.exception.QuotaExceededException;
import com.slop.gpt.exception.RateLimitExceededException;
import com.slop.gpt.exception.ResourceConflictException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RateLimitExceededException.class)
    public ResponseEntity<ErrorResponseDto> handleRateLimitExceeded(RateLimitExceededException ex,
            HttpServletRequest request) {
        ErrorResponseDto payload =
                buildPayload(ex.getMessage(), request.getRequestURI(), List.of());

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.RETRY_AFTER, String.valueOf(ex.getRetryAfterSeconds()));

        return new ResponseEntity<>(payload, headers, HttpStatus.TOO_MANY_REQUESTS);
    }

    @ExceptionHandler(QuotaExceededException.class)
    public ResponseEntity<ErrorResponseDto> handleQuotaExceeded(QuotaExceededException ex,
            HttpServletRequest request) {
        ErrorResponseDto payload =
                buildPayload(ex.getMessage(), request.getRequestURI(), List.of());
        return ResponseEntity.status(HttpStatus.PAYMENT_REQUIRED).body(payload);
    }

    @ExceptionHandler(ResourceConflictException.class)
    public ResponseEntity<ErrorResponseDto> handleConflict(ResourceConflictException ex,
            HttpServletRequest request) {
        ErrorResponseDto payload =
                buildPayload(ex.getMessage(), request.getRequestURI(), List.of());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(payload);
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ErrorResponseDto> handleBadRequest(BadRequestException ex,
            HttpServletRequest request) {
        ErrorResponseDto payload =
                buildPayload(ex.getMessage(), request.getRequestURI(), List.of());
        return ResponseEntity.badRequest().body(payload);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponseDto> handleValidation(MethodArgumentNotValidException ex,
            HttpServletRequest request) {
        List<String> details = new ArrayList<>();
        for (FieldError fieldError : ex.getBindingResult().getFieldErrors()) {
            details.add(fieldError.getField() + ": " + fieldError.getDefaultMessage());
        }

        ErrorResponseDto payload =
                buildPayload("Request validation failed. Please verify your payload.",
                        request.getRequestURI(), details);
        return ResponseEntity.badRequest().body(payload);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorResponseDto> handleConstraintViolations(
            ConstraintViolationException ex, HttpServletRequest request) {
        List<String> details = ex.getConstraintViolations().stream()
                .map(violation -> violation.getPropertyPath() + ": " + violation.getMessage())
                .toList();

        ErrorResponseDto payload =
                buildPayload("Request validation failed. Please verify your parameters.",
                        request.getRequestURI(), details);
        return ResponseEntity.badRequest().body(payload);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDto> handleUnexpected(Exception ex,
            HttpServletRequest request) {
        ErrorResponseDto payload = buildPayload("Unexpected error while processing the request.",
                request.getRequestURI(), List.of());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(payload);
    }

    private ErrorResponseDto buildPayload(String message, String path, List<String> details) {
        return new ErrorResponseDto(message, Instant.now().toString(), path, details);
    }
}
