package app.task_manager.notification.mapper;

import app.task_manager.notification.dto.NotificationDTO;
import app.task_manager.notification.entity.NotificationEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface NotificationMapper {

    @Mapping(source = "recipient.userId", target = "recipientId")
    NotificationDTO toDTO(NotificationEntity entity);

    @Mapping(source = "recipientId", target = "recipient.userId")
    NotificationEntity toEntity(NotificationDTO dto);
}
