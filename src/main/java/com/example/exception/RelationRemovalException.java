package com.example.exception;

public class RelationRemovalException extends RuntimeException {
    public RelationRemovalException(String message) {
        super(message);
    }

    public RelationRemovalException(String message, Throwable cause) {
        super(message, cause);
    }
}
