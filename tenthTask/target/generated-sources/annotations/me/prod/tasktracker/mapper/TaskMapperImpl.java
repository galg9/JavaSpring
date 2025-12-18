package me.prod.tasktracker.mapper;

import java.util.LinkedHashSet;
import java.util.Set;
import javax.annotation.processing.Generated;
import me.prod.tasktracker.dto.TaskDto;
import me.prod.tasktracker.dto.UserDto;
import me.prod.tasktracker.model.Task;
import me.prod.tasktracker.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-12-17T23:12:21+0400",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 17.0.13 (Amazon.com Inc.)"
)
@Component
public class TaskMapperImpl implements TaskMapper {

    @Autowired
    private UserMapper userMapper;

    @Override
    public TaskDto toDto(Task task) {
        if ( task == null ) {
            return null;
        }

        TaskDto taskDto = new TaskDto();

        taskDto.setId( task.getId() );
        taskDto.setName( task.getName() );
        taskDto.setDescription( task.getDescription() );
        taskDto.setCreatedAt( task.getCreatedAt() );
        taskDto.setUpdatedAt( task.getUpdatedAt() );
        taskDto.setStatus( task.getStatus() );
        taskDto.setAuthorId( task.getAuthorId() );
        taskDto.setAssigneeId( task.getAssigneeId() );
        Set<String> set = task.getObserverIds();
        if ( set != null ) {
            taskDto.setObserverIds( new LinkedHashSet<String>( set ) );
        }
        taskDto.setAuthor( userMapper.toDto( task.getAuthor() ) );
        taskDto.setAssignee( userMapper.toDto( task.getAssignee() ) );
        taskDto.setObservers( userSetToUserDtoSet( task.getObservers() ) );

        return taskDto;
    }

    @Override
    public Task toEntity(TaskDto taskDto) {
        if ( taskDto == null ) {
            return null;
        }

        Task task = new Task();

        task.setId( taskDto.getId() );
        task.setName( taskDto.getName() );
        task.setDescription( taskDto.getDescription() );
        task.setCreatedAt( taskDto.getCreatedAt() );
        task.setUpdatedAt( taskDto.getUpdatedAt() );
        task.setStatus( taskDto.getStatus() );
        task.setAuthorId( taskDto.getAuthorId() );
        task.setAssigneeId( taskDto.getAssigneeId() );
        Set<String> set = taskDto.getObserverIds();
        if ( set != null ) {
            task.setObserverIds( new LinkedHashSet<String>( set ) );
        }
        task.setAuthor( userMapper.toEntity( taskDto.getAuthor() ) );
        task.setAssignee( userMapper.toEntity( taskDto.getAssignee() ) );
        task.setObservers( userDtoSetToUserSet( taskDto.getObservers() ) );

        return task;
    }

    protected Set<UserDto> userSetToUserDtoSet(Set<User> set) {
        if ( set == null ) {
            return null;
        }

        Set<UserDto> set1 = new LinkedHashSet<UserDto>( Math.max( (int) ( set.size() / .75f ) + 1, 16 ) );
        for ( User user : set ) {
            set1.add( userMapper.toDto( user ) );
        }

        return set1;
    }

    protected Set<User> userDtoSetToUserSet(Set<UserDto> set) {
        if ( set == null ) {
            return null;
        }

        Set<User> set1 = new LinkedHashSet<User>( Math.max( (int) ( set.size() / .75f ) + 1, 16 ) );
        for ( UserDto userDto : set ) {
            set1.add( userMapper.toEntity( userDto ) );
        }

        return set1;
    }
}
