package com.test.task.service;

import com.test.task.dto.TaskDto;
import com.test.task.dto.UserDto;

import java.util.List;

public interface TaskService {

    List<TaskDto> findAllTasks();

    TaskDto createTask(TaskDto taskDto, UserDto author);

    TaskDto findTaskById(Long taskId) throws Exception;

    TaskDto updateTask(long id, TaskDto taskDto) throws Exception;

    void delete(Long taskId) throws Exception;

//    List<TaskDto> findTaskByUser();
}
