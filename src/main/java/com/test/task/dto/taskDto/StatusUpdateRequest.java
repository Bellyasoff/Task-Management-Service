package com.test.task.dto.taskDto;

import com.test.task.model.enums.Status;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "Status update data")
public class StatusUpdateRequest {
    private Status status;
}
