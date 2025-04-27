package org.example.controllers;

import org.example.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.security.auth.login.CredentialException;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestParam(name = "username") String username, @RequestParam("password") String password) throws CredentialException {
        return ResponseEntity.ok(userService.authenticate(username, password));
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestParam(name = "username") String username, @RequestParam("password") String password) throws CredentialException {
        userService.registerUser(username, password);
        return ResponseEntity.ok("User successfully registered!");
    }

}
