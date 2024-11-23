package com.test.task.service.taskServiceImpl;

import com.test.task.dto.TaskDto;
import com.test.task.dto.UserDto;
import com.test.task.mapper.TaskMapper;
import com.test.task.model.Task;
import com.test.task.model.UserEntity;
import com.test.task.repository.TaskRepository;
import com.test.task.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static com.test.task.mapper.TaskMapper.mapToTask;
import static com.test.task.mapper.TaskMapper.mapToTaskDto;

import static com.test.task.mapper.UserMapper.mapToUser;
import static com.test.task.mapper.UserMapper.mapToUserDto;

@Service
public class TaskServiceImplementation implements TaskService {

    private TaskRepository taskRepository;

    @Autowired
    public TaskServiceImplementation (TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }


    @Override
    public List<TaskDto> findAllTasks() {
        List<Task> tasks = taskRepository.findAll();
        return tasks.stream().map(TaskMapper::mapToTaskDto).collect(Collectors.toList());
    }

    @Override
    public TaskDto createTask(TaskDto taskDto, UserDto author) {
        Task task = mapToTask(taskDto);
        task.setAuthor(mapToUser(author));
        return mapToTaskDto(taskRepository.save(task));
    }

    @Override
    public TaskDto findTaskById(Long taskId) throws Exception {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new Exception("Task not found with id: " + taskId));
        return mapToTaskDto(task);
    }

    @Override
    public TaskDto updateTask(long id, TaskDto taskDto) throws Exception {
        Task existingTask = taskRepository.findById(id)
                .orElseThrow(() -> new Exception("Task not found with id: " + id));
        if (taskDto.getExecutor() != null) {
            UserEntity executor = mapToUser(taskDto.getExecutor());
            existingTask.setExecutor(executor);
        }
        if (taskDto.getHeader() != null) {
            existingTask.setHeader(taskDto.getHeader());
        }
        if (taskDto.getDescription() != null) {
            existingTask.setDescription(taskDto.getDescription());
        }
        if (taskDto.getStatus() != null) {
            existingTask.setStatus(taskDto.getStatus());
        }
        if (taskDto.getPriority() != null) {
            existingTask.setPriority(taskDto.getPriority());
        }
        return mapToTaskDto(taskRepository.save(existingTask));
    }

    @Override
    public void delete(Long taskId) throws Exception {
        if (!taskRepository.existsById(taskId)) {
            throw new Exception("Task not found with id: " + taskId);
        }
        taskRepository.deleteById(taskId);
    }

//    @Override
//    public List<TaskDto> findTaskByUser() {
//        return null;
//    }
}
