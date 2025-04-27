package org.example.service;

import org.example.response.AddFavouritePlaceResponse;
import org.example.response.DeletePlaceResponse;

import javax.security.auth.login.CredentialException;
import java.util.List;

public interface UserService {
    void registerUser(String username, String password);
    String authenticate(String username, String password) throws CredentialException;

    List<String> getFavouritePlaces();

    AddFavouritePlaceResponse addFavouritePlaceById(String placeId);

    DeletePlaceResponse deletePlaceById(String placeId);
}
