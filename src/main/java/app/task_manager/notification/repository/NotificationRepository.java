package app.task_manager.notification.repository;

import app.task_manager.User.entity.UserEntity;
import app.task_manager.notification.entity.NotificationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
@RepositoryRestResource(exported = false)
public interface NotificationRepository extends JpaRepository<NotificationEntity, Long> {
   // List<NotificationEntity> findByRecipientAndReadFalse(UserEntity recipient);
    List<NotificationEntity> findByRecipient_UserIdAndReadFalse(Long recipientId);
}

