package com.onlinequiz.service;

import com.onlinequiz.model.User;
import com.onlinequiz.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // Register a new user
    public User registerUser(User user) {
        return userRepository.save(user);
    }

    // Login user
    public Optional<User> loginUser(String email, String password) {

        Optional<User> user = userRepository.findByEmail(email);

        if (user.isPresent() &&
                user.get().getPassword().equals(password)) {

            return user;
        }

        return Optional.empty();
    }

    // Get user by ID
    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    // Update user profile
    // This saves the updated name, email and password
    public User updateUser(User user) {
        return userRepository.save(user);
    }
}