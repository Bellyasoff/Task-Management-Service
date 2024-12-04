package com.test.task.dto.taskDto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "Comment update data")
public class CommentUpdateRequest {
    private String comment;
}
