package com.example.demo.exception;

public class InvalidDeletionException extends RuntimeException {
    public InvalidDeletionException(String message) {
        super(message);
    }
}