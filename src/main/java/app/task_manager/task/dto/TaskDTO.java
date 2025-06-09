package app.task_manager.task.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.OffsetDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TaskDTO {
    private Long id;

    @NotNull
    private Long authorId;

    @NotBlank
    @Size(min = 3, max = 255)
    private String title;

    @NotBlank
    @Size(max = 4000)
    private String description;

    @FutureOrPresent
    @JsonFormat(shape = JsonFormat.Shape.STRING) // ISO-8601 with offset
    private OffsetDateTime dueDate;

    private Long statusId;
    private Long priorityId;
    private Long parentTaskId;

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private OffsetDateTime completionDate;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private OffsetDateTime createdAt;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private OffsetDateTime updatedAt;

    private List<Long> assignedUsersIds;
    private List<Long> tagIds;
    private List<Long> commentIds;
}
