package app.task_manager.notification.service;

import app.task_manager.User.entity.UserEntity;
import app.task_manager.notification.NotificationInterface.INotificationService;
import app.task_manager.notification.entity.NotificationEntity;
import app.task_manager.notification.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class NotificationService implements INotificationService {

    private final NotificationRepository notificationRepository;

    @Override
    public NotificationEntity createInAppNotification(UserEntity recipient, String message) {
        NotificationEntity notification = NotificationEntity.builder()
                .recipient(recipient)
                .message(message)
                .read(false)
                .build();

        return notificationRepository.save(notification);
    }

    @Override
    public List<NotificationEntity> getUnreadNotifications(Long recipientId) {
        return notificationRepository.findByRecipient_UserIdAndReadFalse(recipientId);
    }



    @Override
    public Optional<NotificationEntity> getNotificationById(Long id) {
        return notificationRepository.findById(id);
    }

    @Override
    public NotificationEntity save(NotificationEntity notification) {
        return notificationRepository.save(notification);
    }


}
