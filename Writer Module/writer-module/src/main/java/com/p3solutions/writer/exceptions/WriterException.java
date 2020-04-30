package com.p3solutions.writer.exceptions;

public class WriterException extends Exception {
    public WriterException(final String message) {
        super(message);
    }
    
    public WriterException(final String message, final Throwable cause) {
        super(message + ": " + cause.getMessage(), cause);
    }
}
