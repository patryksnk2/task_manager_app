package app.task_manager.task_comment.dto;

import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TaskCommentDTO {

    private Long commentId;

    @NotNull(message = "Task ID cannot be null")
    private Long taskId;

    @NotNull(message = "User ID cannot be null")
    private Long userId;

    @NotBlank(message = "Content cannot be blank")
    @Size(max = 4000, message = "Content cannot exceed 4000 characters")
    private String content;

    private Long parentCommentId;

    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;
}
