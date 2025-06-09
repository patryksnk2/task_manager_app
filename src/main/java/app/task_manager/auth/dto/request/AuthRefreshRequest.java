// AuthRefreshRequest.java
package app.task_manager.auth.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class AuthRefreshRequest {
    @NotBlank
    private String refreshToken;
}
