package com.test.task.controller;

import com.test.task.dto.taskDto.AssignExecutorRequest;
import com.test.task.dto.taskDto.CommentUpdateRequest;
import com.test.task.dto.taskDto.StatusUpdateRequest;
import com.test.task.dto.taskDto.TaskDto;
import com.test.task.security.CustomUserDetails;
import com.test.task.service.TaskService;
import com.test.task.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
    public ResponseEntity<List<TaskDto>> getTasks(@AuthenticationPrincipal CustomUserDetails currentUser) {
        System.out.println(currentUser.getAuthorities());
        boolean isAdmin = currentUser.getAuthorities().stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_ADMIN"));

        List<TaskDto> tasks = taskService.getTasksForUser(currentUser.getUsername(), isAdmin);
        return ResponseEntity.ok(tasks);
    }

    // Получить задачу по ID
    @GetMapping("/{id}")
    public ResponseEntity<TaskDto> getTaskById(@PathVariable long id,
                                               @AuthenticationPrincipal CustomUserDetails currentUser) {
        try {
            boolean isAdmin = currentUser.getAuthorities().stream()
                    .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_ADMIN"));

            TaskDto task = taskService.findTaskById(id, currentUser.getUsername(), isAdmin);
            return ResponseEntity.ok(task);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Создать новую задачу
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<TaskDto> createTask(@RequestBody TaskDto taskDto,
                                              @AuthenticationPrincipal CustomUserDetails currentUser) throws Exception {
        TaskDto createdTask = taskService.createTask(taskDto, currentUser.getUsername());

        return ResponseEntity.status(HttpStatus.CREATED).body(createdTask);
    }

    // Обновить задачу (Админ)
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<TaskDto> updateTask(@RequestBody TaskDto taskDto,
                                           @PathVariable long id,
                                              @AuthenticationPrincipal CustomUserDetails currentUser) throws Exception {
            //taskDto.setId(id);
            return ResponseEntity.ok(taskService.updateTask(id, taskDto));
    }

    // Обновить статус задачи (Пользователь)
    @PatchMapping("/{id}/update-status")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<TaskDto> updateTaskStatus(@RequestBody StatusUpdateRequest statusUpdateRequest,
                                                    @PathVariable long id,
                                                    @AuthenticationPrincipal CustomUserDetails currentUser) throws Exception {
        return ResponseEntity.ok(taskService.updateTaskStatus(
                id,
                statusUpdateRequest.getStatus(),
                currentUser.getUsername())
        );
    }

    // Обновить комментарий задачи (Пользователь)
    @PatchMapping("/{id}/update-comment")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<TaskDto> updateTaskComment(@RequestBody CommentUpdateRequest commentUpdateRequest,
                                                    @PathVariable long id,
                                                    @AuthenticationPrincipal CustomUserDetails currentUser) throws Exception {
        return ResponseEntity.ok(taskService.updateTaskComment(
                id,
                commentUpdateRequest.getComment(),
                currentUser.getUsername())
        );
    }

    // Удалить задачу
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
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
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<TaskDto> assignExecutor(@PathVariable long id,
                                                  @RequestBody AssignExecutorRequest request) throws Exception {
        TaskDto updatedTask = taskService.assignExecutor(id, request.getExecutorUsername());

        return ResponseEntity.ok(updatedTask);
    }

}
