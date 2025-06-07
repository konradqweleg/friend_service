package com.example.friends_service.adapter.in.rest.exception_handlers;

import com.example.friends_service.exceptions.UserServiceUnexpectedException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

public class UserServiceExceptionMapper {

    @ExceptionHandler(UserServiceUnexpectedException.class)
    public Mono<ResponseEntity<ErrorResponse>> handleFriendRelationAlreadyExists(UserServiceUnexpectedException ex, ServerWebExchange exchange) {
        return ErrorResponseUtil.generateErrorResponseEntity(ex, exchange, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
