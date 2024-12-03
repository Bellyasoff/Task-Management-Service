package com.test.task.service.taskServiceImpl;

import com.test.task.dto.taskDto.TaskDto;
import com.test.task.mapper.TaskMapper;
import com.test.task.model.Task;
import com.test.task.model.UserEntity;
import com.test.task.model.enums.Status;
import com.test.task.repository.TaskRepository;
import com.test.task.repository.UserRepository;
import com.test.task.service.TaskService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
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

    // Получение задач: USER видит свои задачи, ADMIN — все
    @Override
    public List<TaskDto> getTasksForUser(String username, boolean isAdmin) {
        List<Task> tasks = isAdmin ?
                taskRepository.findAll()
                : taskRepository.findByExecutorUsername(username);

        return tasks.stream().map(TaskMapper::mapToTaskDto).collect(Collectors.toList());
    }


    @Override
    public TaskDto createTask(TaskDto taskDto, String authorUsername) {
        UserEntity author = userRepository.findByUsername(authorUsername)
                .orElseThrow();

        Task task = mapToTask(taskDto, author, null);
        return mapToTaskDto(taskRepository.save(task));
    }

    @Override
    public TaskDto findTaskById(Long taskId, String username, boolean isAdmin) throws Exception {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new Exception("Task not found with id: " + taskId));
        if (!isAdmin) {
            if (!task.getExecutor().getUsername().equals(username)) {
                throw new Exception("You are not an executor of the task");
            }
        }
        return mapToTaskDto(task);
    }

    @Override
    public TaskDto updateTask(long id, TaskDto taskDto) throws Exception {
        Task existingTask = taskRepository.findById(id)
                .orElseThrow(() -> new Exception("Task not found with id: " + id));

        if (existingTask.getStatus() == Status.COMPLETED) {
            throw new Exception("Cannot update completed task");
        }

            existingTask.setHeader(taskDto.getHeader());
            existingTask.setDescription(taskDto.getDescription());
            existingTask.setStatus(taskDto.getStatus());
            existingTask.setPriority(taskDto.getPriority());
            existingTask.setComment(taskDto.getComment());

        return mapToTaskDto(taskRepository.save(existingTask));
    }

    @Override
    public TaskDto updateTaskComment(long id, String comment, String username) throws Exception {
        Task existingTask = taskRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Task not found with id: " + id));

        if (!existingTask.getExecutor().getUsername().equals(username)) {
            throw new Exception("You are not able to comment this task");
        }

        existingTask.setComment(comment);

        return mapToTaskDto(taskRepository.save(existingTask));
    }

    @Override
    public TaskDto updateTaskStatus(long id, Status newStatus, String username) throws Exception {
        Task existingTask = taskRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Task not found with id: " + id));

        if (!existingTask.getExecutor().getUsername().equals(username)) {
            throw new Exception("You are not able to change the status of this task");
        }

        existingTask.setStatus(newStatus);

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
    public TaskDto assignExecutor(Long id, String executorUsername) throws Exception {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Task not found: " + id));

        if (task.getStatus() == Status.COMPLETED) {
            throw new Exception("Cannot assign executor to a completed task");
        }

        UserEntity executor = userRepository.findByUsername(executorUsername)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + executorUsername));

        if (task.getExecutor() != null) {
            throw new IllegalStateException("Task already has executor");
        }

        task.setExecutor(executor);
        task.setStatus(Status.IN_PROCESS);

        return mapToTaskDto(taskRepository.save(task));
    }
}
