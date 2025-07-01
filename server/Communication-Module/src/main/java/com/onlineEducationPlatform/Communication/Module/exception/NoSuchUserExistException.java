package com.onlineEducationPlatform.Communication.Module.exception;

public class NoSuchUserExistException extends RuntimeException {
    
    private String  message;

    public NoSuchUserExistException(String message) {
        super(message);
        this.message = message;
    }

}
