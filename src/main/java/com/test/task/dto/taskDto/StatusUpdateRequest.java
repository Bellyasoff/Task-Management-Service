package com.test.task.dto.taskDto;

import com.test.task.model.enums.Status;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StatusUpdateRequest {
    private Status status;
}
