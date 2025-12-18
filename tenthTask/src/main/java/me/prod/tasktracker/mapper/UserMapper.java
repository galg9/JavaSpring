package me.prod.tasktracker.mapper;

import me.prod.tasktracker.dto.UserDto;
import me.prod.tasktracker.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserMapper {
    UserDto toDto(User user);
    User toEntity(UserDto userDto);
}
