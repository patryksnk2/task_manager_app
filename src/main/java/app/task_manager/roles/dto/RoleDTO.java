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
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RoleDTO {

    private Long roleId;

    @NotBlank(message = "Role cannot be blank")
    @Size(min = 3)
    private RoleName role;

    private List<Long> userIds;
}
