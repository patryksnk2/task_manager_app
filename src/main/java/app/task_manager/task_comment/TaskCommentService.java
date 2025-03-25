package app.task_manager.task_comment;

import app.task_manager.User.UserEntity;
import app.task_manager.User.UserNotFoundException;
import app.task_manager.User.UserRepository;
import app.task_manager.task.TaskEntity;
import app.task_manager.task.TaskNotFoundException;
import app.task_manager.task.TaskRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TaskCommentService {

    private final TaskCommentRepository taskCommentRepository;
    private final TaskCommentMapper taskCommentMapper;
    private final TaskRepository taskRepository;
    private final UserRepository userRepository;

    public TaskCommentService(TaskCommentRepository taskCommentRepository,
                              TaskCommentMapper taskCommentMapper,
                              TaskRepository taskRepository,
                              UserRepository userRepository) {
        this.taskCommentRepository = taskCommentRepository;
        this.taskCommentMapper = taskCommentMapper;
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public TaskCommentDTO create(TaskCommentDTO taskCommentDTO) {

        taskRepository.findById(taskCommentDTO.getTaskId())
                .orElseThrow(() -> new TaskNotFoundException("Task not found"));

        userRepository.findById(taskCommentDTO.getUserId())
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        TaskCommentEntity taskCommentEntity = taskCommentMapper.toEntity(taskCommentDTO);
        return taskCommentMapper.toDTO(taskCommentRepository.saveAndFlush(taskCommentEntity));
    }

    @Transactional
    public TaskCommentDTO update(Long id, TaskCommentDTO taskCommentDTO) {
        TaskCommentEntity taskCommentEntity = taskCommentRepository.findById(id)
                .orElseThrow(() -> new TaskCommentNotFoundException("Task comment not found"));

        taskCommentMapper.updateEntityFromDto(taskCommentEntity, taskCommentDTO);

        if (taskCommentDTO.getTaskId() != null) {
            TaskEntity taskEntity = taskRepository.findById(taskCommentDTO.getTaskId()).orElseThrow(() -> new TaskNotFoundException("task not found"));
            taskCommentEntity.setTask(taskEntity);
        }

        if (taskCommentDTO.getUserId() != null) {
            UserEntity userEntity = userRepository.findById(taskCommentDTO.getUserId()).orElseThrow(() -> new UserNotFoundException("user not found"));
            taskCommentEntity.setUser(userEntity);
        }

        return taskCommentMapper.toDTO(taskCommentRepository.saveAndFlush(taskCommentEntity));
    }

    @Transactional
    public void delete(Long id) {
        if (!taskCommentRepository.existsById(id)) {
            throw new TaskCommentNotFoundException("Task comment not found");
        }
        taskCommentRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public List<TaskCommentDTO> findAll() {
        return taskCommentRepository.findAll()
                .stream()
                .map(taskCommentMapper::toDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    public TaskCommentDTO findById(Long id) {
        TaskCommentEntity taskCommentEntity = taskCommentRepository.findById(id)
                .orElseThrow(() -> new TaskCommentNotFoundException("Task comment not found"));
        return taskCommentMapper.toDTO(taskCommentEntity);
    }
}
