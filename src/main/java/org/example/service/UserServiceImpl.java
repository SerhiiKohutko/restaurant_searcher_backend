package org.example.service;

import org.example.entity.User;
import org.example.exceptions.UsernameAlreadyInUseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService{
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void registerUser(String username, String password) {
        if (userRepository.findUserByUsername(username).isPresent()){
            throw new UsernameAlreadyInUseException();
        }

        User newUser = new User();
        newUser.setUsername(username);
        newUser.setPassword(passwordEncoder.encode(password));

        userRepository.save(newUser);
    }

    @Override
    public String authenticate(String username, String password) {
        return "";
    }
}
