package org.example.exceptions;

public class InvalidPlaceIdException extends RuntimeException {
    public InvalidPlaceIdException(String placeId) {
        super("No place found with provided id: " + placeId);
    }
}
