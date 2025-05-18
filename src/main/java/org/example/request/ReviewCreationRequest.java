package org.example.request;

import lombok.Data;

@Data
public class ReviewCreationRequest {
    private String message;
    private double rating;
    private String placeId;
}
