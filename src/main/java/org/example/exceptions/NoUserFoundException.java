package org.example.exceptions;

public class NoUserFoundException extends RuntimeException {
    public NoUserFoundException(String credentials) {
        super("No user found with provided credentials " + credentials);
    }
}
