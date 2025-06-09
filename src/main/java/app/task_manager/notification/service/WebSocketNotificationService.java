package app.task_manager.notification.service;

import app.task_manager.notification.dto.NotificationDTO;
import app.task_manager.notification.entity.NotificationEntity;
import app.task_manager.notification.mapper.NotificationMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class WebSocketNotificationService {
    private final SimpMessagingTemplate messagingTemplate;
    private final NotificationMapper notificationMapper; // zamienia encję na DTO

    public void sendInAppNotification(NotificationEntity notification) {
        NotificationDTO dto = notificationMapper.toDTO(notification);
        String userDestination = "/queue/notifications";
        messagingTemplate.convertAndSendToUser(
                notification.getRecipient().getUsername(),
                userDestination,
                dto
        );
        log.info("WebSocket notification sent to {}: {}",
                notification.getRecipient().getUsername(), dto);
    }
}
