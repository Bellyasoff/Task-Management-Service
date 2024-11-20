package com.test.task.controller;

import com.test.task.dto.TaskDto;
import com.test.task.dto.UserDto;
import com.test.task.model.Task;
import com.test.task.model.UserEntity;
import com.test.task.service.TaskService;
import com.test.task.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
//import org.springframework.security.core.annotation.AuthenticationPrincipal;
//import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    private final TaskService taskService;
    private final UserService userService;

    @Autowired
    public TaskController(TaskService taskService, UserService userService) {
        this.taskService = taskService;
        this.userService = userService;
    }

    // Получить все задачи
    @GetMapping
    public ResponseEntity<List<TaskDto>> getAllTasks() {
        return ResponseEntity.ok(taskService.findAllTasks());
    }

    // Получить задачу по ID
    @GetMapping("/{id}")
    public ResponseEntity<TaskDto> getTaskById(@PathVariable long id) {
        try {
            TaskDto task = taskService.findTaskById(id);
            return ResponseEntity.ok(task);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Создать новую задачу
    @PostMapping
    public ResponseEntity<TaskDto> createTask(@RequestBody TaskDto taskDto/*,
                                             @AuthenticationPrincipal UserDetails currentUser*/) {
//        UserDto author = userService.findByUsername(currentUser.getUsername());
//        taskDto.setAuthor_id(author.getId());

        return ResponseEntity
                .status(201)
                .body(taskService.createTask(taskDto));
    }

    // Обновить задачу
    @PutMapping("/{id}")
    public ResponseEntity<TaskDto> updateTask(@RequestBody TaskDto taskDto,
                                           @PathVariable long id) {
        try {
            taskDto.setId(id);
            return ResponseEntity.ok(taskService.updateTask(id, taskDto));
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Удалить задачу
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable long id) {
        try {
            taskService.delete(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Назначить исполнителя задачи
    @PatchMapping("/{id}/assign")
    public ResponseEntity<TaskDto> assignExecutor(@PathVariable long id/*,
                                               @AuthenticationPrincipal UserDetails currentUser*/) {
        try {
            TaskDto taskDto = taskService.findTaskById(id);
//            UserDto executor = userService.findByUsername(currentUser.getUsername());
//
//            taskDto.setExecutor(executor);

            return ResponseEntity.ok(taskService.updateTask(id, taskDto));
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

}