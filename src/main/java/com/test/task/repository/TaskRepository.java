package com.test.task.repository;

import com.test.task.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findByExecutorUsername(String username);
    List<Task> findByAuthorUsername(String username);
}
