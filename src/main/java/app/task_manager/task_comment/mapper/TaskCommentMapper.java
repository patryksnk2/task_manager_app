package app.task_manager.task_comment.mapper;


import app.task_manager.User.mapper.UserMapperHelper;
import app.task_manager.task.mapper.TaskMapperHelper;
import app.task_manager.task_comment.dto.TaskCommentCreateDTO;
import app.task_manager.task_comment.dto.TaskCommentDTO;
import app.task_manager.task_comment.entity.TaskCommentEntity;
import org.mapstruct.*;

@Mapper(componentModel = "spring", uses = {TaskMapperHelper.class, UserMapperHelper.class})
public interface TaskCommentMapper {
    @Mapping(source = "task", target = "taskId", qualifiedByName = "mapTaskEntityToId")
    @Mapping(source = "user", target = "userId", qualifiedByName = "mapUserEntityToId")
    @Mapping(source = "parentComment.commentId", target = "parentCommentId")
    TaskCommentDTO toDTO(TaskCommentEntity entity);

    @Mapping(source = "taskId",   target = "task",  qualifiedByName = "mapTaskIdToEntity")
    @Mapping(source = "userId",   target = "user",  qualifiedByName = "mapUserIdToEntity")
    @Mapping(source = "parentCommentId", target = "parentComment", qualifiedByName = "mapCommentIdToEntity")
    TaskCommentEntity toEntity(TaskCommentDTO dto);

    @Mapping(target = "commentId", ignore = true)
    @Mapping(source = "content", target = "content")
    @Mapping(source = "parentCommentId", target = "parentComment", qualifiedByName = "mapCommentIdToEntity")
    @Mapping(target = "task",  ignore = true)
    @Mapping(target = "user",  ignore = true)
    @Mapping(target = "replies", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    TaskCommentEntity toEntityFromCreateDto(TaskCommentCreateDTO createDto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromDto(@MappingTarget TaskCommentEntity taskCommentEntity, TaskCommentDTO taskCommentDTO);
}
