package app.task_manager.notification.controller;

import app.task_manager.User.entity.UserEntity;
import app.task_manager.notification.dto.NotificationDTO;
import app.task_manager.notification.entity.NotificationEntity;
import app.task_manager.notification.NotificationInterface.INotificationService;
import app.task_manager.User.repository.UserRepository;
import app.task_manager.notification.mapper.NotificationMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final INotificationService notificationService;
    private final UserRepository userRepository;
    private final NotificationMapper notificationMapper;
    @GetMapping("/unread")
    public ResponseEntity<List<NotificationDTO>> getUnreadNotifications() {
        Long userId = getCurrentAuthenticatedUser().getUserId();
        List<NotificationDTO> dtos = notificationService.getUnreadNotifications(userId).stream()
                .map(notificationMapper::toDTO)
                .toList();
        return ResponseEntity.ok(dtos);
    }


    @PatchMapping("/{notificationId}/read")
    public ResponseEntity<Void> markAsRead(@PathVariable Long notificationId) {
        NotificationEntity notification = notificationService
                .getNotificationById(notificationId)
                .orElseThrow(() -> new RuntimeException("Notification not found"));

        notification.setRead(true);
        notificationService.save(notification);

        return ResponseEntity.noContent().build();
    }

    private UserEntity getCurrentAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Authenticated user not found"));
    }
}
