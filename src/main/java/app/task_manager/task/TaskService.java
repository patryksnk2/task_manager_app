package app.task_manager.task;

import app.task_manager.User.UserEntity;
import app.task_manager.User.UserRepository;
import app.task_manager.taskAttribute.TaskAttributeEntity;
import app.task_manager.taskAttribute.TaskAttributeRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TaskService {

    private final TaskRepository taskRepository;
    private final TaskMapper taskMapper;
    private final UserRepository userRepository;
    private final TaskAttributeRepository taskAttributeRepository;

    public TaskService(TaskRepository taskRepository, TaskMapper taskMapper, UserRepository userRepository, TaskAttributeRepository taskAttributeRepository) {
        this.taskRepository = taskRepository;
        this.taskMapper = taskMapper;

        this.userRepository = userRepository;
        this.taskAttributeRepository = taskAttributeRepository;
    }

    public List<TaskDTO> findAll() {
        return taskRepository.findAll()
                .stream()
                .map(taskMapper::toDTO)
                .toList();
    }

    @Transactional
    public TaskDTO create(TaskDTO taskDTO) {
        return taskMapper.toDTO(taskRepository.saveAndFlush(taskMapper.toEntity(taskDTO)));
    }

    @Transactional
    public void deleteById(Long id) {
        if (!taskRepository.existsById(id)) {
            throw new TaskNotFoundException("Task not found with id: " + id);
        }
        taskRepository.deleteById(id);
    }

    public TaskDTO findById(Long id) {
        return taskRepository.findById(id).map(taskMapper::toDTO).orElseThrow(() -> new TaskNotFoundException("Task not found with id:" + id));
    }

    @Transactional
    public TaskDTO update(Long id, TaskDTO taskDTO) {
        TaskEntity existingTask = taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException("Task not found with id: " + id));

        taskMapper.updateEntityFromDto(existingTask, taskDTO);

        if (taskDTO.getAssignedUsersIds() != null) {
            List<UserEntity> assignedUsers = userRepository.findAllById(taskDTO.getAssignedUsersIds());
            existingTask.setAssignedUsers(assignedUsers);
        }
        if (taskDTO.getStatusId() != null) {
            TaskAttributeEntity statusId = taskAttributeRepository.findById(taskDTO.getStatusId()).orElse(null);
            existingTask.setStatus(statusId);
        }

        if (taskDTO.getPriorityId() != null) {
            TaskAttributeEntity priorityId = taskAttributeRepository.findById(taskDTO.getPriorityId()).orElse(null);
            existingTask.setStatus(priorityId);
        }
        if (taskDTO.getParentTaskId() != null) {
            TaskEntity parentTask = taskRepository.findById(taskDTO.getParentTaskId()).orElse(null);
            existingTask.setParentTask(parentTask);
        }
        return taskMapper.toDTO(taskRepository.saveAndFlush(existingTask));
    }

}

