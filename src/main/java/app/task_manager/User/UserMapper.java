package app.task_manager.User;

import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {


    @Mapping(source = "userId", target = "userId")
    UserDTO toDTO(UserEntity userEntity);

    @Mapping(source = "userId", target = "userId")
    UserEntity toEntity(UserDTO userDTO);

    List<UserDTO> toDTOList(List<UserEntity> userEntities);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromDto(@MappingTarget UserEntity userEntity, UserDTO userDTO);
}
