package com.example.friends_service.exceptions;

public class UnexpectedFriendServiceException extends RuntimeException {
    public UnexpectedFriendServiceException(String message) {
        super(message);
    }
}

