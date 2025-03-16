package app.task_manager.task;



import app.task_manager.User.UserEntity;
import app.task_manager.User.UserRepository;
import org.mapstruct.Named;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TaskMapperHelper {

    private final UserRepository userRepository;

    public TaskMapperHelper(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    @Named("mapUserEntitiesToIds")
    public List<Long> mapUserEntitiesToIds(List<UserEntity> users) {
        return users.stream().map(UserEntity::getId).toList();
    }
    @Named("mapIdsToUserEntities")
    public List<UserEntity> mapIdsToUserEntities(List<Long> userIds) {
        return userRepository.findAllById(userIds);
    }
}
