package com.test.task.service;

import com.test.task.dto.taskDto.TaskDto;
import com.test.task.model.enums.Status;

import java.util.List;

public interface TaskService {

    List<TaskDto> getTasksForUser(String username, boolean isAdmin);
    List<TaskDto> getTasksByAuthor(String username);
    List<TaskDto> getTasksByExecutor(String username);

    TaskDto createTask(TaskDto taskDto, String author);

    TaskDto findTaskById(Long taskId, String username, boolean isAdmin) throws Exception;

    TaskDto updateTask(long id, TaskDto taskDto) throws Exception;

    TaskDto updateTaskStatus(long id, Status newStatus, String username) throws Exception;

    TaskDto updateTaskComment(long id, String comment, String username) throws Exception;

    void delete(Long taskId) throws Exception;

    TaskDto assignExecutor(Long id, String executor) throws Exception;
}
