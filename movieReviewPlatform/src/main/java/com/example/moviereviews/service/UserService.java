package com.example.moviereviews.service;

import com.example.moviereviews.domain.User;
import com.example.moviereviews.dto.requests.RegisterUserRequest;
import com.example.moviereviews.exception.NotFoundException;
import com.example.moviereviews.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class UserService {

    private final UserRepository users;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository users, PasswordEncoder passwordEncoder) {
        this.users = users;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public User register(RegisterUserRequest req) {
        if (req.getUsername() == null || req.getUsername().isBlank()) {
            throw new IllegalArgumentException("username is required");
        }
        if (req.getPassword() == null || req.getPassword().isBlank()) {
            throw new IllegalArgumentException("password is required");
        }
        if (req.getDisplayName() == null || req.getDisplayName().isBlank()) {
            throw new IllegalArgumentException("displayName is required");
        }

        String username = req.getUsername().trim();

        if (users.findByUsername(username).isPresent()) {
            throw new IllegalArgumentException("Username already exists");
        }

        User u = new User();
        u.setId(UUID.randomUUID());
        u.setUsername(username);
        u.setPassword(passwordEncoder.encode(req.getPassword()));
        u.setDisplayName(req.getDisplayName().trim());
        u.setRole("USER");
        return users.save(u);
    }

    @Transactional
    public void changePassword(User user, String currentPassword, String newPassword) {
        if (!passwordEncoder.matches(currentPassword, user.getPassword())) {
            throw new IllegalArgumentException("Current password is incorrect");
        }

        if (newPassword == null || newPassword.isBlank()) {
            throw new IllegalArgumentException("New password is required");
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        users.save(user);
    }

    @Transactional(readOnly = true)
    public User getOrThrow(UUID id) {
        return users.findById(id).orElseThrow(() -> new NotFoundException("User not found: " + id));
    }
}
