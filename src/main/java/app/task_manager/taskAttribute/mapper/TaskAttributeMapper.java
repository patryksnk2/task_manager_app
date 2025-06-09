package app.task_manager.taskAttribute.mapper;

import app.task_manager.taskAttribute.dto.TaskAttributeDTO;
import app.task_manager.taskAttribute.entity.TaskAttributeEntity;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TaskAttributeMapper {

    TaskAttributeMapper INSTANCE = Mappers.getMapper(TaskAttributeMapper.class);

    TaskAttributeDTO toDTO(TaskAttributeEntity entity);

    TaskAttributeEntity toEntity(TaskAttributeDTO dto);

    List<TaskAttributeDTO> toDTOList(List<TaskAttributeEntity> entities);

    List<TaskAttributeEntity> toEntityList(List<TaskAttributeDTO> dtos);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromDto(@MappingTarget TaskAttributeEntity taskAttributeEntity, TaskAttributeDTO taskAttributeDTO);

}
