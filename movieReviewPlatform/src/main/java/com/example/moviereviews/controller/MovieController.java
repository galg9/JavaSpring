package com.example.moviereviews.controller;

import com.example.moviereviews.dto.MovieDto;
import com.example.moviereviews.dto.requests.CreateMovieRequest;
import com.example.moviereviews.dto.requests.UpdateMovieRequest;
import com.example.moviereviews.service.MovieService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/movies")
@Tag(name = "Movies")
public class MovieController {

    private final MovieService movieService;

    public MovieController(MovieService movieService) {
        this.movieService = movieService;
    }

    @GetMapping
    @Operation(summary = "List movies (paged)")
    public Page<MovieDto> list(Pageable pageable) {
        return movieService.list(pageable);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a movie by id")
    public MovieDto get(@PathVariable UUID id) {
        return movieService.get(id);
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Create a movie (ADMIN only)")
    public ResponseEntity<MovieDto> create(@RequestBody CreateMovieRequest req) {
        MovieDto dto = movieService.create(req);
        return ResponseEntity.status(HttpStatus.CREATED).body(dto);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Update a movie (ADMIN only)")
    public MovieDto update(@PathVariable UUID id, @RequestBody UpdateMovieRequest req) {
        return movieService.update(id, req);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Delete a movie (ADMIN only)")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        movieService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
