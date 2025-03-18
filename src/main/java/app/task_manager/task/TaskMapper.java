package app.task_manager.task;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = TaskMapperHelper.class)
public interface TaskMapper {

    @Mapping(source = "status.id", target = "statusId")
    @Mapping(source = "priority.id", target = "priorityId")
    @Mapping(source = "assignedUsers", target = "assignedUsersIds", qualifiedByName = "mapUserEntitiesToIds")
    @Mapping(source = "parentTask.id", target = "parentTaskId")
    TaskDTO toDTO(TaskEntity task);

    @Mapping(source = "statusId", target = "status.id")
    @Mapping(source = "priorityId", target = "priority.id")
    @Mapping(source = "assignedUsersIds", target = "assignedUsers", qualifiedByName = "mapIdsToUserEntities")
    @Mapping(source = "parentTaskId", target = "parentTask.id")
    TaskEntity toEntity(TaskDTO taskDTO);
}
