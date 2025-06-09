package app.task_manager.task.mapper;

import app.task_manager.User.mapper.UserMapperHelper;
import app.task_manager.task.dto.TaskCreatorDTO;
import app.task_manager.task.dto.TaskDTO;
import app.task_manager.task.dto.TaskUpdateDTO;
import app.task_manager.task.entity.TaskEntity;
import org.mapstruct.*;

@Mapper(componentModel = "spring", uses = {TaskMapperHelper.class, UserMapperHelper.class})
public interface TaskMapper {


    @Mapping(source = "status", target = "statusId", qualifiedByName = "mapTaskAttributeEntityToId")
    @Mapping(source = "priority", target = "priorityId", qualifiedByName = "mapTaskAttributeEntityToId")
    @Mapping(source = "assignedUsers", target = "assignedUsersIds", qualifiedByName = "mapUserEntitiesToIds")
    @Mapping(source = "parentTask", target = "parentTaskId", qualifiedByName = "mapParentTaskEntityToId")
    @Mapping(source = "tags", target = "tagIds", qualifiedByName = "mapTagEntitiesToIds")
    @Mapping(source = "comments", target = "commentIds", qualifiedByName = "mapTaskCommentEntitiesToIds")
    @Mapping(source = "author", target = "authorId", qualifiedByName = "mapUserEntityToId")
    TaskDTO toDTO(TaskEntity task);

    @Mapping(source = "statusId", target = "status", qualifiedByName = "mapTaskAttributeIdToEntity")
    @Mapping(source = "priorityId", target = "priority", qualifiedByName = "mapTaskAttributeIdToEntity")
    @Mapping(source = "assignedUsersIds", target = "assignedUsers", qualifiedByName = "mapIdsToUserEntities")
    @Mapping(source = "parentTaskId", target = "parentTask", qualifiedByName = "mapParentTaskIdToEntity")
    @Mapping(source = "tagIds", target = "tags", qualifiedByName = "mapIdsToTagEntities")
    @Mapping(source = "commentIds", target = "comments", qualifiedByName = "mapIdsToTaskCommentEntities")
    @Mapping(source = "authorId", target = "author", qualifiedByName = "mapUserIdToEntity")
    TaskEntity toEntity(TaskDTO taskDTO);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(source = "statusId", target = "status", qualifiedByName = "mapTaskAttributeIdToEntity")
    @Mapping(source = "priorityId", target = "priority", qualifiedByName = "mapTaskAttributeIdToEntity")
    @Mapping(source = "authorId", target = "author", qualifiedByName = "mapUserIdToEntity")
    @Mapping(source = "parentTaskId", target = "parentTask", qualifiedByName = "mapParentTaskIdToEntity")
    void updateEntityFromUpdateDto(@MappingTarget TaskEntity taskEntity, TaskUpdateDTO updateDTO);

    @Mapping(source = "parentTaskId", target = "parentTask", qualifiedByName = "mapParentTaskIdToEntity")
    TaskEntity fromCreator(TaskCreatorDTO creatorDTO);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromDto(@MappingTarget TaskEntity taskEntity, TaskDTO taskDTO);

}
