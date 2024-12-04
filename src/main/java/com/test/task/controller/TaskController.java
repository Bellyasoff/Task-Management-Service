package com.test.task.controller;

import com.test.task.dto.taskDto.AssignExecutorRequest;
import com.test.task.dto.taskDto.CommentUpdateRequest;
import com.test.task.dto.taskDto.StatusUpdateRequest;
import com.test.task.dto.taskDto.TaskDto;
import com.test.task.security.CustomUserDetails;
import com.test.task.service.TaskService;
import com.test.task.service.UserService;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tasks")
@Tag(name = "Tasks operations for authorized users")
@OpenAPIDefinition(info = @Info(
        title = "Task Management Service API - Definition",
        description = "Operations descriptions"))
public class TaskController {

    private final TaskService taskService;

    @Autowired
    public TaskController(TaskService taskService, UserService userService) {
        this.taskService = taskService;
    }

    // Получить все задачи
    @ApiResponse(responseCode = "200", description = "All tasks returned",
    content = {
            @Content(mediaType = "application/json",
            schema = @Schema(implementation = TaskDto.class))
    })
    @Operation(summary = "Get all tasks")
    @GetMapping
    public ResponseEntity<List<TaskDto>> getTasks(@AuthenticationPrincipal CustomUserDetails currentUser) {
        boolean isAdmin = currentUser.getAuthorities().stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_ADMIN"));

        List<TaskDto> tasks = taskService.getTasksForUser(currentUser.getUsername(), isAdmin);
        return ResponseEntity.ok(tasks);
    }

    // Получить все задачи конкретного автора
    @ApiResponse(responseCode = "200", description = "Task by author     returned",
            content = {
                    @Content(mediaType = "application/json",
                            schema = @Schema(implementation = TaskDto.class))
            })
    @Operation(summary = "Get all author`s tasks; Only for ADMIN")
    @GetMapping("/author/{username}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<TaskDto>> getTasksByAuthor(@PathVariable String username) {
        List<TaskDto> tasks = taskService.getTasksByAuthor(username);
        return ResponseEntity.ok(tasks);
    }

    // Получить все задачи конкретного исполнителя
    @ApiResponse(responseCode = "200", description = "Task by executor returned",
            content = {
                    @Content(mediaType = "application/json",
                            schema = @Schema(implementation = TaskDto.class))
            })
    @Operation(summary = "Get all executor`s tasks; Only for ADMIN")
    @GetMapping("/executor/{username}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<TaskDto>> getTasksByExecutor(@PathVariable String username) {
        List<TaskDto> tasks = taskService.getTasksByExecutor(username);
        return ResponseEntity.ok(tasks);
    }

    // Получить задачу по id
    @ApiResponse(responseCode = "200", description = "Task by ID returned",
            content = {
                    @Content(mediaType = "application/json",
                            schema = @Schema(implementation = TaskDto.class))
            })
    @Operation(summary = "Get task by ID")
    @GetMapping("/{id}")
    public ResponseEntity<TaskDto> getTaskById(@Parameter(description = "Task`s ID")
                                                   @PathVariable long id,
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
    @ApiResponse(responseCode = "201", description = "Task created",
            content = {
                    @Content(mediaType = "application/json",
                            schema = @Schema(implementation = TaskDto.class))
            })
    @Operation(summary = "Create new task; Only for ADMIN")
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<TaskDto> createTask(@RequestBody TaskDto taskDto,
                                              @AuthenticationPrincipal CustomUserDetails currentUser) throws Exception {
        TaskDto createdTask = taskService.createTask(taskDto, currentUser.getUsername());

        return ResponseEntity.status(HttpStatus.CREATED).body(createdTask);
    }

    // Обновить задачу (Админ)
    @ApiResponse(responseCode = "200", description = "Task updated",
            content = {
                    @Content(mediaType = "application/json",
                            schema = @Schema(implementation = TaskDto.class))
            })
    @Operation(summary = "Update existing task; Only for ADMIN")
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<TaskDto> updateTask(@RequestBody TaskDto taskDto,
                                           @PathVariable long id) throws Exception {
            return ResponseEntity.ok(taskService.updateTask(id, taskDto));
    }

    // Обновить статус задачи (Пользователь)
    @ApiResponse(responseCode = "200", description = "Task`s status updated",
            content = {
                    @Content(mediaType = "application/json",
                            schema = @Schema(implementation = TaskDto.class))
            })
    @Operation(summary = "Update existing task`s status; Only for USER")
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
    @ApiResponse(responseCode = "200", description = "Task`s comment updated",
            content = {
                    @Content(mediaType = "application/json",
                            schema = @Schema(implementation = TaskDto.class))
            })
    @Operation(summary = "Update existing task`s comment; Only for USER")
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
    @ApiResponse(responseCode = "204", description = "Task deleted")
    @Operation(summary = "Delete existing task; Only for ADMIN")
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
    @ApiResponse(responseCode = "200", description = "Task deleted",
            content = {
                    @Content(mediaType = "application/json",
                            schema = @Schema(implementation = TaskDto.class))
            })
    @Operation(summary = "Assign executor to an existing task; Only for ADMIN")
    @PatchMapping("/{id}/assign")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<TaskDto> assignExecutor(@PathVariable long id,
                                                  @RequestBody AssignExecutorRequest request) throws Exception {
        TaskDto updatedTask = taskService.assignExecutor(id, request.getExecutorUsername());

        return ResponseEntity.ok(updatedTask);
    }

}
