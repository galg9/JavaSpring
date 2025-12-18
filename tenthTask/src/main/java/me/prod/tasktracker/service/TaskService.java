package me.prod.tasktracker.service;

import lombok.RequiredArgsConstructor;
import me.prod.tasktracker.model.Task;
import me.prod.tasktracker.model.User;
import me.prod.tasktracker.repository.TaskRepository;
import me.prod.tasktracker.repository.UserRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TaskService {
    private final TaskRepository taskRepository;
    private final UserRepository userRepository;

    public Flux<Task> findAll() {
        return taskRepository.findAll()
                .flatMap(this::enrichTaskWithUsers);
    }

    public Mono<Task> findById(String id) {
        return taskRepository.findById(id)
                .flatMap(this::enrichTaskWithUsers);
    }

    public Mono<Task> create(Task task) {
        task.setCreatedAt(Instant.now());
        task.setUpdatedAt(Instant.now());
        return taskRepository.save(task)
                .flatMap(this::enrichTaskWithUsers);
    }

    public Mono<Task> update(String id, Task task) {
        return taskRepository.findById(id)
                .flatMap(existingTask -> {
                    existingTask.setName(task.getName());
                    existingTask.setDescription(task.getDescription());
                    existingTask.setStatus(task.getStatus());
                    existingTask.setAssigneeId(task.getAssigneeId());
                    existingTask.setObserverIds(task.getObserverIds());
                    existingTask.setUpdatedAt(Instant.now());
                    return taskRepository.save(existingTask);
                })
                .flatMap(this::enrichTaskWithUsers);
    }

    public Mono<Task> addObserver(String taskId, String observerId) {
        return taskRepository.findById(taskId)
                .flatMap(task -> {
                    if (task.getObserverIds() == null) {
                        task.setObserverIds(new HashSet<>());
                    }
                    task.getObserverIds().add(observerId);
                    task.setUpdatedAt(Instant.now());
                    return taskRepository.save(task);
                })
                .flatMap(this::enrichTaskWithUsers);
    }

    public Mono<Void> deleteById(String id) {
        return taskRepository.deleteById(id);
    }

    private Mono<Task> enrichTaskWithUsers(Task task) {
        Mono<User> authorMono = task.getAuthorId() != null
                ? userRepository.findById(task.getAuthorId())
                : Mono.empty();

        Mono<User> assigneeMono = task.getAssigneeId() != null
                ? userRepository.findById(task.getAssigneeId())
                : Mono.empty();

        Mono<Set<User>> observersMono = task.getObserverIds() != null && !task.getObserverIds().isEmpty()
                ? userRepository.findAllById(task.getObserverIds()).collect(Collectors.toSet())
                : Mono.just(new HashSet<>());

        return Mono.zip(
                        authorMono.defaultIfEmpty(new User()),
                        assigneeMono.defaultIfEmpty(new User()),
                        observersMono
                )
                .map(tuple -> {
                    User author = tuple.getT1();
                    User assignee = tuple.getT2();
                    Set<User> observers = tuple.getT3();

                    if (author.getId() != null) {
                        task.setAuthor(author);
                    }
                    if (assignee.getId() != null) {
                        task.setAssignee(assignee);
                    }
                    task.setObservers(observers);

                    return task;
                });
    }
}
