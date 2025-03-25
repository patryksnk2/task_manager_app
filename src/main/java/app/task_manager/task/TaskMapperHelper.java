package app.task_manager.task;


import app.task_manager.User.UserEntity;
import app.task_manager.User.UserRepository;
import app.task_manager.taskAttribute.TaskAttributeEntity;
import app.task_manager.taskAttribute.TaskAttributeRepository;

import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;


@Component
public class TaskMapperHelper {
    private final TaskAttributeRepository taskAttributeRepository;
    private final UserRepository userRepository;
    private final TaskRepository taskRepository;

    @Autowired
    public TaskMapperHelper(TaskAttributeRepository taskAttributeRepository, UserRepository userRepository, TaskRepository taskRepository) {
        this.taskAttributeRepository = taskAttributeRepository;
        this.userRepository = userRepository;
        this.taskRepository = taskRepository;
    }

    @Named("mapTaskAttributeIdToEntity")
    public TaskAttributeEntity mapTaskAttributeIdToEntity(Long taskAttributeId) {
        return taskAttributeId != null ? taskAttributeRepository.findById(taskAttributeId).orElse(null) : null;
    }

    @Named("mapTaskAttributeEntityToId")
    public Long mapTaskAttributeEntityToId(TaskAttributeEntity taskAttributeEntity) {
        return taskAttributeEntity != null ? taskAttributeEntity.getId() : null;
    }

    @Named("mapParentTaskIdToEntity")
    public TaskEntity mapParentTaskIdToEntity(Long parentTaskId) {
        return parentTaskId != null ? taskRepository.findById(parentTaskId).orElse(null) : null;
    }

    @Named("mapParentTaskEntityToId")
    public Long mapParentTaskEntityToId(TaskEntity parentTaskEntity) {
        return parentTaskEntity != null ? parentTaskEntity.getId() : null;
    }

    @Named("mapTaskEntityToId")
    public Long mapTaskEntityToId(TaskEntity taskEntity) {
        return taskEntity != null ? taskEntity.getId() : null;
    }

    @Named("mapTaskIdToEntity")
    public TaskEntity mapTaskIdToEntity(Long id) {
        return id != null ? taskRepository.findById(id).orElseThrow(() -> new TaskNotFoundException("task not found")) : null;
    }

    @Named("mapIdsToUserEntities")
    public List<UserEntity> mapIdsToUserEntities(List<Long> userIds) {
        return userIds != null ? userRepository.findAllById(userIds) : Collections.emptyList();
    }

    @Named("mapUserEntitiesToIds")
    public List<Long> mapUserEntitiesToIds(List<UserEntity> userEntities) {
        return userEntities != null ? userEntities.stream().map(UserEntity::getUserId).toList() : Collections.emptyList();
    }
}
