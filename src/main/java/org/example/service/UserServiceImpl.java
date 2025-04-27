package org.example.service;

import org.example.entity.User;
import org.example.exceptions.NoUserFoundException;
import org.example.exceptions.UsernameAlreadyInUseException;
import org.example.jwt.JwtProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.security.auth.login.CredentialException;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService{
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JwtProvider jwtProvider;

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
    public String authenticate(String username, String password) throws CredentialException {
        Optional<User> user = userRepository.findUserByUsername(username);

        if (user.isEmpty()){
            throw new NoUserFoundException(username);
        }

        if (!passwordEncoder.matches(password, user.get().getPassword())) {
            throw new CredentialException("Wrong password");
        }

        return jwtProvider.generateToken(user.get());
    }
}
