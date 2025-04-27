package com.example.friends_service.exceptions;

public class FriendRelationAlreadyExists extends RuntimeException {
    public FriendRelationAlreadyExists(String message) {
        super(message);
    }
}
