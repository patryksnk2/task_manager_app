package app.task_manager.notification.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class NotificationDTO {
    private Long id;
    private String message;
    private boolean read;
    private Long recipientId;
    private LocalDateTime createdAt;
}
