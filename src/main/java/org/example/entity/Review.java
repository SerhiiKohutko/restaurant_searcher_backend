package org.example.entity;

import jakarta.persistence.*;


public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    private String message;
    private double rating;

}
