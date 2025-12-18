package me.prod.tasktracker.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import me.prod.tasktracker.dto.TaskDto;
import me.prod.tasktracker.mapper.TaskMapper;
import me.prod.tasktracker.service.TaskService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/tasks")
@RequiredArgsConstructor
public class TaskController {
    private final TaskService taskService;
    private final TaskMapper taskMapper;

    @GetMapping
    public Flux<TaskDto> getAllTasks() {
        return taskService.findAll()
                .map(taskMapper::toDto);
    }

    @GetMapping("/{id}")
    public Mono<TaskDto> getTaskById(@PathVariable String id) {
        return taskService.findById(id)
                .map(taskMapper::toDto);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<TaskDto> createTask(@Valid @RequestBody TaskDto taskDto) {
        return taskService.create(taskMapper.toEntity(taskDto))
                .map(taskMapper::toDto);
    }

    @PutMapping("/{id}")
    public Mono<TaskDto> updateTask(@PathVariable String id, @Valid @RequestBody TaskDto taskDto) {
        return taskService.update(id, taskMapper.toEntity(taskDto))
                .map(taskMapper::toDto);
    }

    @PostMapping("/{taskId}/observers/{observerId}")
    public Mono<TaskDto> addObserver(@PathVariable String taskId, @PathVariable String observerId) {
        return taskService.addObserver(taskId, observerId)
                .map(taskMapper::toDto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> deleteTask(@PathVariable String id) {
        return taskService.deleteById(id);
    }
}
