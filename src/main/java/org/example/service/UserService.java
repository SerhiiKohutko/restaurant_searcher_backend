package org.example.service;

public interface UserService {
    void registerUser(String username, String password);
    String authenticate(String username, String password);

}
