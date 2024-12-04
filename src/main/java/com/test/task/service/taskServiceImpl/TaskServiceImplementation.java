package com.test.task.service.taskServiceImpl;

import com.test.task.dto.taskDto.TaskDto;
import com.test.task.mapper.TaskMapper;
import com.test.task.model.Task;
import com.test.task.model.UserEntity;
import com.test.task.model.enums.Status;
import com.test.task.repository.TaskRepository;
import com.test.task.repository.UserRepository;
import com.test.task.service.TaskService;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static com.test.task.mapper.TaskMapper.mapToTask;
import static com.test.task.mapper.TaskMapper.mapToTaskDto;

@Service
@Log4j
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
    public List<TaskDto> getTasksByAuthor(String username) {
        List<Task> tasks = taskRepository.findByAuthorUsername(username);

        return tasks.stream().map(TaskMapper::mapToTaskDto).collect(Collectors.toList());
    }

    @Override
    public List<TaskDto> getTasksByExecutor(String username) {
        List<Task> tasks = taskRepository.findByExecutorUsername(username);

        return tasks.stream().map(TaskMapper::mapToTaskDto).collect(Collectors.toList());
    }

    // Создать новую задачу
    @Override
    public TaskDto createTask(TaskDto taskDto, String authorUsername) {
        log.info(authorUsername + " is creating a new task");

        UserEntity author = userRepository.findByUsername(authorUsername)
                .orElseThrow(() -> {
                    log.error(authorUsername + " not found");
                    return new UsernameNotFoundException("username not found");
                });

        Task task = mapToTask(taskDto, author, null);
        return mapToTaskDto(taskRepository.save(task));
    }

    @Override
    public TaskDto findTaskById(Long taskId, String username, boolean isAdmin) throws Exception {
        log.info(username + " is searching task with id: " + taskId);
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> {
                    log.error("Task not found with id: " + taskId);
                    return new Exception("Task not found with id: " + taskId);
                });
        if (!isAdmin) {
            if (!task.getExecutor().getUsername().equals(username)) {
                log.error(username + " is not an executor of the task");
                throw new Exception("You are not an executor of the task");
            }
        }
        return mapToTaskDto(task);
    }

    @Override
    public TaskDto updateTask(long id, TaskDto taskDto) throws Exception {
        log.info("Updating task with id: " + id);
        Task existingTask = taskRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Task not found with id: " + id);
                    return new Exception("Task not found with id: " + id);
                });

        if (existingTask.getStatus() == Status.COMPLETED) {
            log.error("Trying to update task " + id + " status, that has been already COMPLETED");
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
        log.info(username + " is updating task`s comment with id: " + id);
        Task existingTask = taskRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Task not found with id: " + id);
                    return new Exception("Task not found with id: " + id);
                });

        if (!existingTask.getExecutor().getUsername().equals(username)) {
            log.error(username + " is not an executor of the task");
            throw new Exception("You are not able to comment this task");
        }

        existingTask.setComment(comment);

        return mapToTaskDto(taskRepository.save(existingTask));
    }

    @Override
    public TaskDto updateTaskStatus(long id, Status newStatus, String username) throws Exception {
        log.info(username + " is updating task`s status with id: " + id);
        Task existingTask = taskRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Task not found with id: " + id);
                    return new Exception("Task not found with id: " + id);
                });

        if (!existingTask.getExecutor().getUsername().equals(username)) {
            log.error(username + " is not an executor of the task");
            throw new Exception("You are not able to change the status of this task");
        }
        if (existingTask.getStatus() == Status.COMPLETED) {
            log.error("Trying to update task " + id + " status, that has been already COMPLETED");
            throw new Exception("Cannot update completed task");
        }

        existingTask.setStatus(newStatus);

        return mapToTaskDto(taskRepository.save(existingTask));
    }

    @Override
    public void delete(Long taskId) throws Exception {
        log.info("Deleting task with id: " + taskId);
        if (!taskRepository.existsById(taskId)) {
            log.error("Task not found with id: " + taskId);
            throw new Exception("Task not found with id: " + taskId);
        }
        taskRepository.deleteById(taskId);
    }

    @Override
    public TaskDto assignExecutor(Long id, String executorUsername) throws Exception {
        log.info("Assigning executor: " + executorUsername + "; for the task with id: " + id);
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Task not found with id: " + id);
                    return new Exception("Task not found with id: " + id);
                });

        if (task.getStatus() == Status.COMPLETED) {
            log.error("Trying to assign executor to a task:  " + id + " ;that has been already COMPLETED");
            throw new Exception("Cannot assign executor to a completed task");
        }

        UserEntity executor = userRepository.findByUsername(executorUsername)
                .orElseThrow(() -> {
                    log.error("User not found with username: " + executorUsername);
                    return new UsernameNotFoundException("User not found with username: " + executorUsername);
                });

        if (task.getExecutor() != null) {
            log.error("Trying to assign executor to a task: " + id + " ;that has an executor");
            throw new IllegalStateException("Task already has executor");
        }

        task.setExecutor(executor);
        log.debug("Setting status \"IN PROCESS\" ");
        task.setStatus(Status.IN_PROCESS);

        return mapToTaskDto(taskRepository.save(task));
    }
}
