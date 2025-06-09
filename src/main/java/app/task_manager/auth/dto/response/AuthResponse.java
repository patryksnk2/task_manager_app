// src/main/java/app/task_manager/auth/AuthResponse.java
package app.task_manager.auth.dto.response;

import app.task_manager.User.dto.UserDTO;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AuthResponse {
    private String accessToken;
    private String refreshToken;
    private UserDTO user;
}
