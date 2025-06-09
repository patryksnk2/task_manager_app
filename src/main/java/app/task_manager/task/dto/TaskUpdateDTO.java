package app.task_manager.task.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TaskUpdateDTO {

    private String title;

    private String description;

    private OffsetDateTime dueDate;

    private Long statusId;

    private Long priorityId;

    private OffsetDateTime completionDate;

    private Long authorId;

    private List<Long> assignedUsersIds;

    private Long parentTaskId;

    private List<Long> tagIds;

    private List<Long> commentIds;
}
