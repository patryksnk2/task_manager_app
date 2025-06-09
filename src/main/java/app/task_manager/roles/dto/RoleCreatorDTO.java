package app.task_manager.roles.dto;

import app.task_manager.roles.enums.RoleName;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RoleCreatorDTO {
    @NotBlank(message = "Role cannot be empty")
    @Size(min = 3)
    RoleName role;
}
