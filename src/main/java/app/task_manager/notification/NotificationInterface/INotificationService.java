package app.task_manager.notification.NotificationInterface;

import app.task_manager.User.entity.UserEntity;
import app.task_manager.notification.entity.NotificationEntity;

import java.util.List;
import java.util.Optional;

public interface INotificationService {
    NotificationEntity createInAppNotification(UserEntity recipient, String message);
    List<NotificationEntity> getUnreadNotifications(Long recipientId);
    Optional<NotificationEntity> getNotificationById(Long id);
    NotificationEntity save(NotificationEntity notification);
}
