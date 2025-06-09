package app.task_manager.User.mapper;

import app.task_manager.User.dto.UserDTO;
import app.task_manager.User.entity.UserEntity;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring",uses = UserMapperHelper.class)
public interface UserMapper {


    @Mapping(source = "userId", target = "userId")
    @Mapping(source = "roles",target = "rolesIds",qualifiedByName = "mapRolesToIds")
    UserDTO toDTO(UserEntity userEntity);

    @Mapping(source = "userId", target = "userId")
    @Mapping(source = "rolesIds",target = "roles",qualifiedByName = "mapIdsToRoles")
    UserEntity toEntity(UserDTO userDTO);

    List<UserDTO> toDTOList(List<UserEntity> userEntities);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromDto(@MappingTarget UserEntity userEntity, UserDTO userDTO);
}
