package app.task_manager.User.dto;// app.task_manager.auth.UserRegistrationDTO


import jakarta.validation.constraints.*;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserRegistrationDTO {
    @NotBlank
    private String username;

    @Email
    @NotBlank
    private String email;

    @Pattern(
            regexp = "^(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$",
            message = "Password must be at least 8 characters long, contain one uppercase letter, one digit, and one special character."
    )
    @Size(min = 8, message = "Password must be at least 8 characters long.")
    private String password;
}
