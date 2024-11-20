package com.test.task.service;

import com.test.task.dto.TaskDto;
import com.test.task.model.Task;

import java.util.List;

public interface TaskService {

    List<TaskDto> findAllTasks();

    TaskDto createTask(TaskDto taskDto);

    TaskDto findTaskById(Long taskId) throws Exception;

    TaskDto updateTask(long id, TaskDto taskDto) throws Exception;

    void delete(Long taskId) throws Exception;

    List<TaskDto> findTaskByUser();
}
