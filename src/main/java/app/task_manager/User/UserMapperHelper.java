package app.task_manager.User;

import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapperHelper {
    private final UserRepository userRepository;

    @Autowired
    public UserMapperHelper(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Named("mapUserEntityToId")
    public Long mapUserEntityToId(UserEntity userEntity) {
        return userEntity != null ? userEntity.getUserId() : null;
    }

    @Named("mapUserIdToEntity")
    public UserEntity mapUserIdToEntity(Long user_id) {
        return user_id != null ? userRepository.findById(user_id)
                .orElseThrow(() -> new UserNotFoundException("User not found")) : null;
    }
}
