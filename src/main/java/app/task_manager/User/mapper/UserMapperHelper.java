package app.task_manager.User.mapper;

import app.task_manager.User.repository.UserRepository;
import app.task_manager.User.entity.UserEntity;
import app.task_manager.User.exception.UserNotFoundException;
import app.task_manager.roles.entity.RoleEntity;
import app.task_manager.roles.repository.RoleRepository;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

@Component
public class UserMapperHelper {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    @Autowired
    public UserMapperHelper(UserRepository userRepository, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
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
    @Named("mapUserIdsToEntities")
    public List<Long> mapUserIdsToEntities(List<UserEntity> userEntities){
        return userEntities!=null?userEntities.stream().map(UserEntity::getUserId).toList():Collections.emptyList();
    }
    @Named("mapUserEntitiesToIds")
    public List<UserEntity> mapUserEntitiesToIds(List<Long> userIds){
        return userIds!=null?userRepository.findAllById(userIds):Collections.emptyList();
    }
    @Named("mapRolesToIds")
    public List<Long> mapRolesToIds(List<RoleEntity> roles){
       return roles!=null?roles.stream().map(RoleEntity::getRoleId).toList(): Collections.emptyList();
    }
    @Named("mapIdsToRoles")
    public List<RoleEntity> mapIdsToRoles(List<Long> rolesIds){
        return rolesIds!=null?roleRepository.findAllById(rolesIds):Collections.emptyList();
    }
}
