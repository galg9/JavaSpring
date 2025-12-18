package me.prod.tasktracker.mapper;

import me.prod.tasktracker.dto.TaskDto;
import me.prod.tasktracker.model.Task;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, uses = {UserMapper.class})
public interface TaskMapper {
    TaskDto toDto(Task task);
    Task toEntity(TaskDto taskDto);
}
