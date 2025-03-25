package app.task_manager.task_comment;


import app.task_manager.User.UserMapperHelper;
import app.task_manager.task.TaskMapperHelper;
import org.mapstruct.*;

@Mapper(componentModel = "spring", uses = {TaskMapperHelper.class, UserMapperHelper.class})
public interface TaskCommentMapper {
    @Mapping(source = "task", target = "taskId", qualifiedByName = "mapTaskEntityToId")
    @Mapping(source = "user", target = "userId", qualifiedByName = "mapUserEntityToId")
    TaskCommentDTO toDTO(TaskCommentEntity taskCommentEntity);

    @Mapping(source = "taskId", target = "task", qualifiedByName = "mapTaskIdToEntity")
    @Mapping(source = "userId", target = "user", qualifiedByName = "mapUserIdToEntity")
    TaskCommentEntity toEntity(TaskCommentDTO taskCommentDTO);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromDto(@MappingTarget TaskCommentEntity taskCommentEntity, TaskCommentDTO taskCommentDTO);
}
