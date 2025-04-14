package com.example.friends_service.exceptions;

public class UserToCreateRelationNotFoundException extends RuntimeException {
    public UserToCreateRelationNotFoundException(String message) {
        super(message);
    }
}