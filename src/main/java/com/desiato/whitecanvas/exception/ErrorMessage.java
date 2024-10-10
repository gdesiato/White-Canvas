package com.desiato.whitecanvas.exception;

public record ErrorMessage(String message) {
    public ErrorMessage {
        if (message == null || message.isBlank()) {
            throw new IllegalArgumentException("Error message cannot be null or blank");
        }
    }
}