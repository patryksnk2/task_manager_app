package app.task_manager.tag;

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
public class TagDTO {

    private Long tag_id;

    @NotBlank(message = "Name cannot be blank")
    @Size(min = 3, message = "Name require at least 3 letters")
    private String name;
}
