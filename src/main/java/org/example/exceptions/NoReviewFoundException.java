package org.example.exceptions;

public class NoReviewFoundException extends RuntimeException {
    public NoReviewFoundException(String message) {
        super("No review found with id: " + message);
    }
}
