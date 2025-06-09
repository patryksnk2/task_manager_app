package app.task_manager.roles.mapper;

import app.task_manager.User.mapper.UserMapperHelper;
import app.task_manager.roles.dto.RoleDTO;
import app.task_manager.roles.entity.RoleEntity;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring", uses = UserMapperHelper.class)
public interface RoleMapper {

    @Mapping(source = "roleId", target = "roleId")
    @Mapping(source = "users", target = "userIds", qualifiedByName = "mapUserIdsToEntities")
    RoleDTO toDto(RoleEntity roleEntity);

    @Mapping(source = "roleId", target = "roleId")
    @Mapping(source = "userIds", target = "users", qualifiedByName = "mapUserEntitiesToIds")
    RoleEntity toEntity(RoleDTO roleDTO);

    List<RoleDTO> toDTOList(List<RoleEntity> roles);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromDTO(@MappingTarget RoleEntity roleEntity, RoleDTO roleDTO);
}
