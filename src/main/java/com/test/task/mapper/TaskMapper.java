package com.test.task.mapper;

import com.test.task.dto.TaskDto;
import com.test.task.model.Task;

public class TaskMapper {

    public static Task mapToTask(TaskDto task) {
        Task taskDto = Task.builder()
                .id(task.getId())
                .header(task.getHeader())
                .description(task.getDescription())
                .status(task.getStatus())
                .priority(task.getPriority())
                .comment(task.getComment())
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
                .build();
        return taskDto;
    }
}
