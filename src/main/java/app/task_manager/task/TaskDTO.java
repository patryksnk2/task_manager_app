package app.task_manager.task;

import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TaskDTO {
    private Long id;

    @NotBlank(message = "Title cannot be blank")
    @Size(min = 3, max = 255)
    private String title;

    @NotBlank(message = "Description cannot be blank")
    @Size(max = 4000, message = "Description cannot exceed 4000 characters")
    private String description;

    @FutureOrPresent(message = "Due date must be in the present or future")
    private LocalDateTime dueDate;

    private Long statusId;
    private Long priorityId;
    private Long parentTaskId;
    private LocalDateTime completionDate;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<Long> assignedUsersIds;
}
