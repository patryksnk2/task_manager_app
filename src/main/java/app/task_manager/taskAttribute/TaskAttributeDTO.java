package app.task_manager.taskAttribute;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TaskAttributeDTO {
    private Long id;

    @NotBlank(message = "Category cannot be blank")
    @Size(min = 3, max = 255)
    private String category;

    @NotBlank(message = "Name cannot be blank")
    @Size(min = 3, max = 255)
    private String name;
}
