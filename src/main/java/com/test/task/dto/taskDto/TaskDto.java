package com.test.task.dto.taskDto;

import com.test.task.model.enums.Priority;
import com.test.task.model.enums.Status;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TaskDto {
    private Long id;
    private String header;
    private String description;
    private Status status;
    private Priority priority;
    private String comment;

    private String authorUsername;
    private String executorUsername;
}
