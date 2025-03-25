package app.task_manager.tag;


import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;

@Service
public class TagService {
    private final TagRepository tagRepository;
    private final TagMapper tagMapper;

    public TagService(TagRepository tagRepository, TagMapper tagMapper) {
        this.tagRepository = tagRepository;
        this.tagMapper = tagMapper;
    }

    @Transactional
    public TagDTO create(TagDTO tagDTO) {
        TagEntity tagEntity = tagMapper.toEntity(tagDTO);
        return tagMapper.toDTO(tagRepository.saveAndFlush(tagEntity));
    }

    @Transactional
    public TagDTO update(Long id, TagDTO tagDTO) {
        TagEntity tagEntity = tagRepository.findById(id)
                .orElseThrow(() -> new TagNotFoundException("tag not found"));

        tagMapper.updateEntityFromDTO(tagEntity, tagDTO);

        return tagMapper.toDTO(tagRepository.saveAndFlush(tagEntity));
    }

    @Transactional
    public void delete(Long id) {
        if (!tagRepository.existsById(id)) {
            throw new TagNotFoundException("tag not found");
        }
        tagRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public List<TagDTO> findAll() {
        return tagRepository.findAll()
                .stream()
                .map(tagMapper::toDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    public TagDTO findById(Long id) {
        TagEntity tagEntity = tagRepository.findById(id)
                .orElseThrow(() -> new TagNotFoundException("tag not found"));
        return tagMapper.toDTO(tagEntity);
    }


}
