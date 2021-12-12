package com.msaid.userservice.exception;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(String message) {
        super(message);
    }
    public UserNotFoundException() {
        super();
    }
}
