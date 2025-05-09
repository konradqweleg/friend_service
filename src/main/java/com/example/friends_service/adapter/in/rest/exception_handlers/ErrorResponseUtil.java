package com.example.friends_service.adapter.in.rest.exception_handlers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

public class ErrorResponseUtil {
    private static ErrorResponse generateErrorResponse(Throwable ex, ServerWebExchange exchange, HttpStatus status) {
        return new ErrorResponse.Builder()
                .timestamp(LocalDateTime.now())
                .status(status.value())
                .error(status.getReasonPhrase())
                .message(ex.getMessage())
                .path(exchange.getRequest().getPath().value())
                .build();
    }
    public static Mono<ResponseEntity<ErrorResponse>> generateErrorResponseEntity(Throwable ex, ServerWebExchange exchange, HttpStatus status) {
        ErrorResponse errorResponse = generateErrorResponse(ex, exchange, status);
        return Mono.just(ResponseEntity.status(status).body(errorResponse));
    }
}