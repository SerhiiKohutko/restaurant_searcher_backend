package org.example.entity;

import jakarta.persistence.*;

public class Place {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String placeId;
}
