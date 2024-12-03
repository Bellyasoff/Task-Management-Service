package com.test.task.mapper;

import com.test.task.dto.taskDto.TaskDto;
import com.test.task.model.Task;
import com.test.task.model.UserEntity;
import com.test.task.model.enums.Status;

public class TaskMapper {

    public static Task mapToTask(TaskDto task, UserEntity author, UserEntity executor) {
        Task taskDto = Task.builder()
                .id(task.getId())
                .header(task.getHeader())
                .description(task.getDescription())
                .status(task.getStatus() != null ? task.getStatus() : Status.WAITING)
                .priority(task.getPriority())
                .comment(task.getComment())
                .author(author)
                .executor(executor)
                .build();
        return taskDto;
    }

    public static TaskDto mapToTaskDto(Task task) {
        TaskDto taskDto = TaskDto.builder()
                .id(task.getId())
                .header(task.getHeader())
                .description(task.getDescription())
                .status(task.getStatus())
                .priority(task.getPriority())
                .comment(task.getComment())
                .authorUsername(task.getAuthor() != null ? task.getAuthor().getUsername() : null)
                .executorUsername(task.getExecutor() != null ? task.getExecutor().getUsername() : null)
                .build();
        return taskDto;
    }
}
