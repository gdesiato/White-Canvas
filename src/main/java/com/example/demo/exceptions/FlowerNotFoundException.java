package com.example.demo.exceptions;

public class FlowerNotFoundException extends RuntimeException {

    public FlowerNotFoundException(String message) {
        super(message);
    }
}
