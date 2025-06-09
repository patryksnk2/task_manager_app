package app.task_manager.task_comment.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class TaskCommentUpdateDTO {
    @NotBlank
    String content;
}
