package app.task_manager.task;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = TaskMapperHelper.class)
public interface TaskMapper {

    @Mapping(source = "taskAttribute.id", target = "taskAttributesId")
    @Mapping(source = "assignedUsers", target = "assignedUsersIds", qualifiedByName = "mapUserEntitiesToIds")
    TaskDTO toDTO(TaskEntity task);

    @Mapping(source = "taskAttributesId", target = "taskAttribute.id")
    @Mapping(source = "assignedUsersIds", target = "assignedUsers", qualifiedByName = "mapIdsToUserEntities")
    TaskEntity toEntity(TaskDTO taskDTO);
}
