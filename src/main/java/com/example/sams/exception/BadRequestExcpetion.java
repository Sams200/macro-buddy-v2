package com.example.sams.exception;

public class BadRequestExcpetion extends RuntimeException {
    public BadRequestExcpetion(String message) {
        super(message);
    }
}
