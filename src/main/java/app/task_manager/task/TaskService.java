package app.task_manager.task;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TaskService {

    private final TaskRepository taskRepository;
    private final TaskMapper taskMapper;
    //private final UserRepository userRepository;

    public TaskService(TaskRepository taskRepository, TaskMapper taskMapper) {
        this.taskRepository = taskRepository;
        this.taskMapper = taskMapper;

    }

    public List<TaskDTO> findAll() {
        return taskRepository.findAll()
                .stream()
                .map(taskMapper::toDTO)
                .toList();
    }

    @Transactional
    public TaskDTO create(TaskDTO taskDTO) {
        return taskMapper.toDTO(taskRepository.save(taskMapper.toEntity(taskDTO)));
    }

    @Transactional
    public void deleteById(Long id) {
        if (!taskRepository.existsById(id)) {
            throw new TaskNotFoundException("Task not found with id: " + id);
        }
    }

    public TaskDTO findById(Long id) {
        return taskRepository.findById(id).map(taskMapper::toDTO).orElseThrow(() -> new TaskNotFoundException("Task not found with id:" + id));
    }

//    @Transactional
//    public TaskDTO update(Long id, TaskDTO taskDTO) {
//        Task existingTask = taskRepository.findById(id)
//                .orElseThrow(() -> new TaskNotFoundException("Task not found with id: " + id));
//
//        existingTask.setTitle(taskDTO.getTitle());
//        existingTask.setDescription(taskDTO.getDescription());
//        existingTask.setDueDate(taskDTO.getDueDate());
//
//        // Zamiast tworzyć nową instancję TaskAttributes, pobieramy pełny obiekt z bazy danych
//        TaskAttribute taskAttributes = taskRepository.findTaskAttributesById(taskDTO.getTaskAttributesId())
//                .orElseThrow(() -> new TaskNotFoundException("TaskAttributes not found with id: " + taskDTO.getTaskAttributesId()));
//
//        // Zaktualizowanie TaskAttributes w obiekcie Task
//        existingTask.setTaskAttribute(taskAttributes);
//
//        existingTask.setCompletionDate(taskDTO.getCompletionDate());
//
//        // Mapowanie użytkowników na podstawie ich identyfikatorów
//        List<app.task_manager.user.UserEntity> assignedUsers = taskMapper.mapIdsToUserEntities(taskDTO.getAssignedUsersIds(), userRepository);
//        existingTask.setAssignedUsers(assignedUsers);
//
//        existingTask = taskRepository.save(existingTask);
//        return taskMapper.toDTO(existingTask);
//    }
//poprwaić bo to ma byc TaskAttributeId bo to jest tylko ID jako refernmacja do tabeli TaskAttributes usunac to S na końcu
    // Posprawdzać wszystko
    //Kontroller DO Task Ogarnąć
    // i samemmu ogarnac User
    // i trolle
}

