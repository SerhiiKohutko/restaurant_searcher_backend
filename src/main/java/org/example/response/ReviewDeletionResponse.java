package org.example.response;

public class ReviewDeletionResponse {
    private final String message;

    public ReviewDeletionResponse(Long reviewId) {
        this.message = "Review with id " + reviewId + " deleted.";
    }
}
