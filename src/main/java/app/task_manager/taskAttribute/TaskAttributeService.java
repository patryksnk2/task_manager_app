package app.task_manager.taskAttribute;

import app.task_manager.task.TaskRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TaskAttributeService {
    private final TaskAttributeRepository taskAttributeRepository;
    private final TaskAttributeMapper taskAttributeMapper;

    public TaskAttributeService(TaskAttributeRepository taskAttributeRepository, TaskAttributeMapper taskAttributeMapper) {
        this.taskAttributeRepository = taskAttributeRepository;
        this.taskAttributeMapper = taskAttributeMapper;
    }

    @Transactional
    public TaskAttributeDTO create(TaskAttributeDTO taskAttributeDTO) {
        TaskAttributeEntity entity = taskAttributeMapper.toEntity(taskAttributeDTO);
        return taskAttributeMapper.toDTO(taskAttributeRepository.save(entity));
    }


    public List<TaskAttributeDTO> getAll() {
        return taskAttributeRepository.findAll()
                .stream()
                .map(taskAttributeMapper::toDTO)
                .collect(Collectors.toList());
    }


    public TaskAttributeDTO getById(Long id) {
        TaskAttributeEntity entity = taskAttributeRepository.findById(id)
                .orElseThrow(() -> new TaskAttributeNotFoundException("Attribute not found"));
        return taskAttributeMapper.toDTO(entity);
    }

    @Transactional
    public TaskAttributeDTO update(Long id, TaskAttributeDTO taskAttributeDTO) {
        TaskAttributeEntity taskAttributeEntity = taskAttributeRepository.findById(id)
                .orElseThrow(() -> new TaskAttributeNotFoundException("Attribute not found"));

        taskAttributeMapper.updateEntityFromDto(taskAttributeEntity, taskAttributeDTO);
        return taskAttributeMapper.toDTO(taskAttributeRepository.saveAndFlush(taskAttributeEntity));
    }

    @Transactional
    public void delete(Long id) {
        if (!taskAttributeRepository.existsById(id)) {
            throw new TaskAttributeNotFoundException("Attribute not found");
        }
        taskAttributeRepository.deleteById(id);
    }
}
