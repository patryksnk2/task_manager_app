package app.task_manager.tag.dto;

import app.task_manager.tag.enums.TagName;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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

    @NotNull(message = "Name cannot be null")
    private TagName name;
}
