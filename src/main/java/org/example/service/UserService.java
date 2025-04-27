package org.example.service;

import javax.security.auth.login.CredentialException;

public interface UserService {
    void registerUser(String username, String password);
    String authenticate(String username, String password) throws CredentialException;
}
