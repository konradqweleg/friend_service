package com.example.friends_service.adapter.in.rest.exception_handlers;

import com.example.friends_service.exceptions.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

public class FriendExceptionMapper {

    @ExceptionHandler(FriendRelationAlreadyExists.class)
    public Mono<ResponseEntity<ErrorResponse>> handleFriendRelationAlreadyExists(FriendRelationAlreadyExists ex, ServerWebExchange exchange) {
        return ErrorResponseUtil.generateErrorResponseEntity(ex, exchange, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(FriendServiceParsingException.class)
    public Mono<ResponseEntity<ErrorResponse>> handleFriendServiceParsingException(FriendServiceParsingException ex, ServerWebExchange exchange) {
        return ErrorResponseUtil.generateErrorResponseEntity(ex, exchange, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(GeneralFriendServiceException.class)
    public Mono<ResponseEntity<ErrorResponse>> handleGeneralFriendServiceException(GeneralFriendServiceException ex, ServerWebExchange exchange) {
        return ErrorResponseUtil.generateErrorResponseEntity(ex, exchange, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(RepositoryException.class)
    public Mono<ResponseEntity<ErrorResponse>> handleRepositoryException(RepositoryException ex, ServerWebExchange exchange) {
        return ErrorResponseUtil.generateErrorResponseEntity(ex, exchange, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(UnexpectedFriendServiceException.class)
    public Mono<ResponseEntity<ErrorResponse>> handleUnexpectedFriendServiceException(UnexpectedFriendServiceException ex, ServerWebExchange exchange) {
        return ErrorResponseUtil.generateErrorResponseEntity(ex, exchange, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public Mono<ResponseEntity<ErrorResponse>> handleUserNotFoundException(UserNotFoundException ex, ServerWebExchange exchange) {
        return ErrorResponseUtil.generateErrorResponseEntity(ex, exchange, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UserToCreateRelationNotFoundException.class)
    public Mono<ResponseEntity<ErrorResponse>> handleUserToCreateRelationNotFoundException(UserToCreateRelationNotFoundException ex, ServerWebExchange exchange) {
        return ErrorResponseUtil.generateErrorResponseEntity(ex, exchange, HttpStatus.BAD_REQUEST);
    }
}
