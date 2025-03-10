package app.task_manager.task;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.PastOrPresent;
import java.time.LocalDate;
import java.time.LocalDateTime;

public record Task(
        @NotNull Long taskId,

        @NotNull @Size(max = 255, message = "Title must be less than or equal to 255 characters")
        String title,

        @Size(max = 4000, message = "Description must be less than or equal to 4000 characters")
        String description,

        LocalDate dueDate,

        Long taskAttributesId,

        LocalDateTime completionDate,

        LocalDateTime createdAt,

        LocalDateTime updatedAt
) {
}
