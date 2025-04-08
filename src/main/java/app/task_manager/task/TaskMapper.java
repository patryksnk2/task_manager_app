package app.task_manager.task;

import org.mapstruct.*;

@Mapper(componentModel = "spring", uses = TaskMapperHelper.class)
public interface TaskMapper {


    @Mapping(source = "status", target = "statusId", qualifiedByName = "mapTaskAttributeEntityToId")
    @Mapping(source = "priority", target = "priorityId", qualifiedByName = "mapTaskAttributeEntityToId")
    @Mapping(source = "assignedUsers", target = "assignedUsersIds", qualifiedByName = "mapUserEntitiesToIds")
    @Mapping(source = "parentTask", target = "parentTaskId", qualifiedByName = "mapParentTaskEntityToId")
    @Mapping(source = "tags",target = "tagIds",qualifiedByName = "mapTagEntitiesToIds")
    @Mapping(source = "comments",target ="commentIds",qualifiedByName = "mapTaskCommentEntitiesToIds")
    TaskDTO toDTO(TaskEntity task);

    @Mapping(source = "statusId", target = "status", qualifiedByName = "mapTaskAttributeIdToEntity")
    @Mapping(source = "priorityId", target = "priority", qualifiedByName = "mapTaskAttributeIdToEntity")
    @Mapping(source = "assignedUsersIds", target = "assignedUsers", qualifiedByName = "mapIdsToUserEntities")
    @Mapping(source = "parentTaskId", target = "parentTask", qualifiedByName = "mapParentTaskIdToEntity")
    @Mapping(source = "tagIds",target = "tags",qualifiedByName = "mapIdsToTagEntities")
    @Mapping(source = "commentIds",target ="comments",qualifiedByName = "mapIdsToTaskCommentEntities")
    TaskEntity toEntity(TaskDTO taskDTO);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromDto(@MappingTarget TaskEntity taskEntity, TaskDTO taskDTO);

}
