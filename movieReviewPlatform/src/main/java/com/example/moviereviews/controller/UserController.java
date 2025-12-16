package com.example.moviereviews.controller;

import com.example.moviereviews.domain.User;
import com.example.moviereviews.dto.requests.ChangePasswordRequest;
import com.example.moviereviews.dto.requests.RegisterUserRequest;
import com.example.moviereviews.repository.UserRepository;
import com.example.moviereviews.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
@Tag(name = "Users")
public class UserController {

    private final UserService userService;
    private final UserRepository userRepository;

    public UserController(UserService userService, UserRepository userRepository) {
        this.userService = userService;
        this.userRepository = userRepository;
    }

    @PostMapping("/register")
    @Operation(summary = "Register a new user with role USER")
    public ResponseEntity<?> register(@RequestBody RegisterUserRequest req) {
        User u = userService.register(req);
        return ResponseEntity
                .created(URI.create("/api/users/" + u.getId()))
                .body(Map.of(
                        "id", u.getId(),
                        "username", u.getUsername(),
                        "displayName", u.getDisplayName(),
                        "role", u.getRole(),
                        "createdAt", u.getCreatedAt()
                ));
    }

    @PostMapping("/change-password")
    @Operation(summary = "Change password for authenticated user")
    public ResponseEntity<?> changePassword(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody ChangePasswordRequest req) {

        User user = userRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        userService.changePassword(user, req.getCurrentPassword(), req.getNewPassword());

        return ResponseEntity.ok(Map.of("message", "Password changed successfully"));
    }

    @GetMapping("/me")
    @Operation(summary = "Get current authenticated user info")
    public ResponseEntity<?> getCurrentUser(@AuthenticationPrincipal UserDetails userDetails) {
        User user = userRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        return ResponseEntity.ok(Map.of(
                "id", user.getId(),
                "username", user.getUsername(),
                "displayName", user.getDisplayName(),
                "role", user.getRole(),
                "createdAt", user.getCreatedAt()
        ));
    }
}
