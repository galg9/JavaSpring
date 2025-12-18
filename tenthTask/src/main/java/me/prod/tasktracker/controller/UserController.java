package me.prod.tasktracker.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import me.prod.tasktracker.dto.UserDto;
import me.prod.tasktracker.mapper.UserMapper;
import me.prod.tasktracker.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final UserMapper userMapper;

    @GetMapping
    public Flux<UserDto> getAllUsers() {
        return userService.findAll()
                .map(userMapper::toDto);
    }

    @GetMapping("/{id}")
    public Mono<UserDto> getUserById(@PathVariable String id) {
        return userService.findById(id)
                .map(userMapper::toDto);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<UserDto> createUser(@Valid @RequestBody UserDto userDto) {
        return userService.create(userMapper.toEntity(userDto))
                .map(userMapper::toDto);
    }

    @PutMapping("/{id}")
    public Mono<UserDto> updateUser(@PathVariable String id, @Valid @RequestBody UserDto userDto) {
        return userService.update(id, userMapper.toEntity(userDto))
                .map(userMapper::toDto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> deleteUser(@PathVariable String id) {
        return userService.deleteById(id);
    }
}
