package com.eighttracks.exception;

public class InvalidTrackRequestException extends RuntimeException {

    public InvalidTrackRequestException(String message) {
        super(message);
    }
}
