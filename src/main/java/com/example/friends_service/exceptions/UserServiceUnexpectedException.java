package com.example.friends_service.exceptions;

public class UserServiceUnexpectedException extends RuntimeException {
    public UserServiceUnexpectedException(String message) {
        super(message);
    }

    public UserServiceUnexpectedException(String message, Throwable cause) {
        super(message, cause);
    }
}
