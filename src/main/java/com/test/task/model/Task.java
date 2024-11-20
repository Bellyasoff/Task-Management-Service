package com.test.task.model;

import com.test.task.model.enums.Priority;
import com.test.task.model.enums.Status;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "tasks")
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String header;
    private String description;
    private Status status;
    private Priority priority;
    private String comment;

    @ManyToOne
    @JoinColumn(name = "author_id")
    private UserEntity author;

    @ManyToOne
    @JoinColumn(name = "executor_id")
    private UserEntity executor;
}
