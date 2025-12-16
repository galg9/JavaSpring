package com.example.moviereviews.controller;

import com.example.moviereviews.dto.ReviewDto;
import com.example.moviereviews.dto.requests.CreateReviewRequest;
import com.example.moviereviews.dto.requests.UpdateReviewRequest;
import com.example.moviereviews.repository.UserRepository;
import com.example.moviereviews.service.ReviewService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping
@Tag(name = "Reviews")
public class ReviewController {

    private final ReviewService reviewService;
    private final UserRepository userRepository;

    public ReviewController(ReviewService reviewService, UserRepository userRepository) {
        this.reviewService = reviewService;
        this.userRepository = userRepository;
    }

    @GetMapping("/api/movies/{movieId}/reviews")
    @Operation(summary = "List reviews for a movie (paged)")
    public Page<ReviewDto> listByMovie(@PathVariable UUID movieId, Pageable pageable) {
        return reviewService.listByMovie(movieId, pageable);
    }

    @GetMapping("/api/reviews/{id}")
    @Operation(summary = "Get a review by id")
    public ReviewDto get(@PathVariable UUID id) {
        return reviewService.get(id);
    }

    @PostMapping("/api/reviews")
    @Operation(summary = "Create a review (one review per movie per user)")
    public ResponseEntity<ReviewDto> create(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody CreateReviewRequest req) {
        UUID userId = userRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"))
                .getId();
        ReviewDto dto = reviewService.create(userId, req);
        return ResponseEntity.status(HttpStatus.CREATED).body(dto);
    }

    @PutMapping("/api/reviews/{id}")
    @Operation(summary = "Update a review (owner only)")
    public ReviewDto update(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable UUID id,
            @RequestBody UpdateReviewRequest req) {
        UUID userId = userRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"))
                .getId();
        return reviewService.update(userId, id, req);
    }

    @DeleteMapping("/api/reviews/{id}")
    @Operation(summary = "Delete a review (owner only)")
    public ResponseEntity<Void> delete(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable UUID id) {
        UUID userId = userRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"))
                .getId();
        reviewService.delete(userId, id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/api/reviews/{id}/admin")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Delete any review (ADMIN only)")
    public ResponseEntity<Void> deleteAsAdmin(@PathVariable UUID id) {
        reviewService.deleteAsAdmin(id);
        return ResponseEntity.noContent().build();
    }
}
