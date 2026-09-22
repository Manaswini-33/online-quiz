package com.onlinequiz.controller;

import com.onlinequiz.model.User;
import com.onlinequiz.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "*")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    // Register a new user
    @PostMapping("/register")
    public ResponseEntity<User> register(@RequestBody User user) {
        User savedUser = userService.registerUser(user);
        return ResponseEntity.ok(savedUser);
    }

    // Login user
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User user) {

        Optional<User> loggedInUser =
                userService.loginUser(
                        user.getEmail(),
                        user.getPassword()
                );

        if (loggedInUser.isPresent()) {
            return ResponseEntity.ok(loggedInUser.get());
        }

        return ResponseEntity.status(401)
                .body("Invalid email or password");
    }

    // Update user profile
    @PutMapping("/{id}")
    public ResponseEntity<?> updateProfile(
            @PathVariable Long id,
            @RequestBody User updatedUser) {

        // Find existing user
        Optional<User> existingUser =
                userService.getUserById(id);

        // If user does not exist
        if (existingUser.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        User user = existingUser.get();

        // Update name
        if (updatedUser.getName() != null &&
                !updatedUser.getName().trim().isEmpty()) {

            user.setName(updatedUser.getName());
        }

        // Update email
        if (updatedUser.getEmail() != null &&
                !updatedUser.getEmail().trim().isEmpty()) {

            user.setEmail(updatedUser.getEmail());
        }

        // Update password
        if (updatedUser.getPassword() != null &&
                !updatedUser.getPassword().trim().isEmpty()) {

            user.setPassword(updatedUser.getPassword());
        }

        // Save updated user
        User savedUser = userService.updateUser(user);

        return ResponseEntity.ok(savedUser);
    }
}