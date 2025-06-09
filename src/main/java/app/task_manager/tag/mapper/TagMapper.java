package app.task_manager.tag.mapper;

import app.task_manager.tag.dto.TagDTO;
import app.task_manager.tag.entity.TagEntity;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TagMapper {

    @Mapping(source = "tag_id", target = "tag_id")
    TagEntity toEntity(TagDTO tagDTO);

    @Mapping(source = "tag_id", target = "tag_id")
    TagDTO toDTO(TagEntity tagEntity);

    List<TagDTO> toDTOList(List<TagEntity> tagEntities);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromDTO(@MappingTarget TagEntity tagEntity, TagDTO tagDTO);
}
