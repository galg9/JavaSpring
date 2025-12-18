package me.prod.tasktracker.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import me.prod.tasktracker.model.TaskStatus;

import java.time.Instant;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TaskDto {
    private String id;

    @NotBlank(message = "Task name is required")
    private String name;

    private String description;

    private Instant createdAt;

    private Instant updatedAt;

    @NotNull(message = "Task status is required")
    private TaskStatus status;

    @NotBlank(message = "Author ID is required")
    private String authorId;

    private String assigneeId;

    private Set<String> observerIds;

    private UserDto author;
    private UserDto assignee;
    private Set<UserDto> observers;
}
