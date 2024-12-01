package com.test.task.service.taskServiceImpl;

import com.test.task.dto.TaskDto;
import com.test.task.dto.UserDto;
import com.test.task.mapper.TaskMapper;
import com.test.task.model.Task;
import com.test.task.model.UserEntity;
import com.test.task.model.enums.Status;
import com.test.task.repository.TaskRepository;
import com.test.task.repository.UserRepository;
import com.test.task.service.TaskService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static com.test.task.mapper.TaskMapper.mapToTask;
import static com.test.task.mapper.TaskMapper.mapToTaskDto;

@Service
public class TaskServiceImplementation implements TaskService {

    private final TaskRepository taskRepository;
    private final UserRepository userRepository;

    @Autowired
    public TaskServiceImplementation (TaskRepository taskRepository,
                                      UserRepository userRepository) {
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
    }


    @Override
    public List<TaskDto> findAllTasks() {
        List<Task> tasks = taskRepository.findAll();
        return tasks.stream().map(TaskMapper::mapToTaskDto).collect(Collectors.toList());
    }


    @Override
    public TaskDto createTask(TaskDto taskDto, String authorUsername) {
        UserEntity author = userRepository.findByUsername(authorUsername);

        Task task = mapToTask(taskDto, author, null);
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

    @Override
    public TaskDto assignExecutor(Long id, String executorUsername) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Task not found: " + id));

        if (task.getExecutor() != null) {
            throw new IllegalStateException("Task already has executor");
        }

        UserEntity executor = userRepository.findByUsername(executorUsername);

        task.setExecutor(executor);
        task.setStatus(Status.IN_PROCESS);
        return mapToTaskDto(taskRepository.save(task));
    }
}
