package app.task_manager.task.service;

import app.task_manager.User.entity.UserEntity;
import app.task_manager.User.repository.UserRepository;
import app.task_manager.notification.NotificationInterface.INotificationService;
import app.task_manager.notification.entity.NotificationEntity;
import app.task_manager.notification.service.EmailNotificationService;
import app.task_manager.notification.service.WebSocketNotificationService;
import app.task_manager.tag.dto.TagDTO;
import app.task_manager.tag.entity.TagEntity;
import app.task_manager.tag.repository.TagRepository;
import app.task_manager.task.aggregate.TaskAggregateService;
import app.task_manager.task.dto.TaskCreatorDTO;
import app.task_manager.task.dto.TaskDTO;
import app.task_manager.task.dto.TaskUpdateDTO;
import app.task_manager.task.entity.TaskEntity;
import app.task_manager.task.exception.TaskDeleteNotAllowed;
import app.task_manager.task.exception.TaskNotFoundException;
import app.task_manager.task.mapper.TaskMapper;
import app.task_manager.task.repository.TaskRepository;
import app.task_manager.taskAttribute.entity.TaskAttributeEntity;
import app.task_manager.taskAttribute.enums.TaskAttributeCategory;
import app.task_manager.taskAttribute.enums.TaskPriority;
import app.task_manager.taskAttribute.enums.TaskStatus;
import app.task_manager.taskAttribute.exception.TaskAttributeNotFoundException;
import app.task_manager.taskAttribute.repository.TaskAttributeRepository;
import app.task_manager.task_comment.dto.TaskCommentDTO;
import app.task_manager.task_comment.entity.TaskCommentEntity;
import app.task_manager.task_comment.mapper.TaskCommentMapper;
import app.task_manager.task_comment.repository.TaskCommentRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@AllArgsConstructor
@Slf4j
public class TaskService {

    private final TaskRepository taskRepository;
    private final TaskMapper taskMapper;
    private final TaskAggregateService taskAggregateService;
    private final UserRepository userRepository;
    private final TaskAttributeRepository taskAttributeRepository;
    private final TagRepository tagRepository;
    private final TaskCommentRepository taskCommentRepository;
    private final TaskCommentMapper commentMapper;
    private final INotificationService notificationService;
    private final EmailNotificationService emailNotificationService;
    private final WebSocketNotificationService webSocketNotificationService;

    public List<TaskDTO> findAll() {
        log.info("findAll – fetching all tasks");
        List<TaskDTO> result = taskRepository.findAll().stream().map(taskMapper::toDTO).toList();
        log.debug("findAll – returned {} tasks", result.size());
        return result;
    }

    @Transactional
    public TaskDTO create(TaskCreatorDTO creatorDTO) {
        log.info("create – starting task creation: {}", creatorDTO);
        TaskEntity entity = taskMapper.fromCreator(creatorDTO);
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        log.debug("create – current user: {}", username);

        TaskAttributeEntity priority = taskAttributeRepository.findByCategoryAndName(TaskAttributeCategory.PRIORITY, TaskPriority.LOW.name()).orElseThrow(() -> {
            log.error("create – default priority not found: {}", TaskPriority.LOW);
            return new TaskAttributeNotFoundException("priority with name: " + TaskPriority.LOW.name() + " was not found");
        });

        TaskAttributeEntity status = taskAttributeRepository.findByCategoryAndName(TaskAttributeCategory.STATUS, TaskStatus.NEW.name()).orElseThrow(() -> {
            log.error("create – default status not found: {}", TaskStatus.NEW);
            return new TaskAttributeNotFoundException("status with name: " + TaskStatus.NEW.name() + " was not found");
        });

        UserEntity current = userRepository.findByUsername(username).orElseThrow(() -> {
            log.error("create – user not found: {}", username);
            return new IllegalStateException("user with username: " + username + " doesn't exist");
        });

        TaskEntity parentTask = null;
        if (creatorDTO.getParentTaskId() != null) {
            log.debug("create – fetching parent task id={}", creatorDTO.getParentTaskId());
            parentTask = taskRepository.findById(creatorDTO.getParentTaskId()).orElseThrow(() -> {
                log.error("create – parent task not found: {}", creatorDTO.getParentTaskId());
                return new TaskNotFoundException("Parent task with id: " + creatorDTO.getParentTaskId() + " was not found");
            });
        }

        entity.setAuthor(current);
        entity.setStatus(status);
        entity.setPriority(priority);
        entity.setAssignedUsers(new ArrayList<>());
        entity.getAssignedUsers().add(current);
        entity.setParentTask(parentTask);

        TaskEntity saved = taskRepository.saveAndFlush(entity);
        TaskDTO dto = taskMapper.toDTO(saved);
        log.info("create – task created with id={}", dto.getId());
        return dto;
    }

    @Transactional
    public void delete(Long id) {
        log.info("delete – starting deletion of taskId={}", id);
        TaskEntity task = taskRepository.findById(id).orElseThrow(() -> {
            log.error("delete – task not found: {}", id);
            return new TaskNotFoundException("task with id: " + id + " was not found");
        });

        String currentUser = SecurityContextHolder.getContext().getAuthentication().getName();
        log.debug("delete – current user: {}", currentUser);
        UserEntity author = userRepository.findByUsername(currentUser).orElseThrow(() -> {
            log.error("delete – author user not found: {}", currentUser);
            return new UsernameNotFoundException("user with username: " + currentUser + " was not found");
        });

        if (!Objects.equals(task.getAuthor(), author)) {
            log.warn("delete – user={} not allowed to delete taskId={}", currentUser, id);
            throw new TaskDeleteNotAllowed("Only author of the task can delete it");
        }

        taskRepository.deleteById(id);
        log.info("delete – taskId={} deleted", id);
    }

    public TaskDTO findById(Long id) {
        log.info("findById – fetching taskId={}", id);
        TaskDTO dto = taskRepository.findById(id).map(taskMapper::toDTO).orElseThrow(() -> {
            log.error("findById – task not found: {}", id);
            return new TaskNotFoundException("Task not found with id: " + id);
        });
        log.debug("findById – returning taskId={}", id);
        return dto;
    }

    @Transactional
    public TaskDTO update(Long id, TaskUpdateDTO updateDTO) {
        log.info("update – starting update of taskId={}, payload={}", id, updateDTO);
        TaskEntity existing = taskRepository.findById(id).orElseThrow(() -> {
            log.error("update – task not found: {}", id);
            return new TaskNotFoundException("Task not found with id: " + id);
        });

        taskMapper.updateEntityFromUpdateDto(existing, updateDTO);

        if (updateDTO.getAssignedUsersIds() != null) {
            log.debug("update – updating assigned users for taskId={}", id);
            existing.setAssignedUsers(userRepository.findAllById(updateDTO.getAssignedUsersIds()));
        }
        if (updateDTO.getTagIds() != null) {
            log.debug("update – updating tags for taskId={}", id);
            existing.setTags(tagRepository.findAllById(updateDTO.getTagIds()));
        }
        if (updateDTO.getCommentIds() != null) {
            log.debug("update – updating comments for taskId={}", id);
            existing.setComments(taskCommentRepository.findAllById(updateDTO.getCommentIds()));
        }

        updateAndValidateAuthor(existing, updateDTO);
        updateAndValidateStatus(existing, updateDTO);
        updateAndValidatePriority(existing, updateDTO);
        updateParentTask(existing, updateDTO);

        TaskEntity updated = taskRepository.saveAndFlush(existing);
        TaskDTO dto = taskMapper.toDTO(updated);
        log.info("update – taskId={} updated successfully", dto.getId());
        return dto;
    }

    private void updateAndValidateAuthor(TaskEntity existing, TaskUpdateDTO updateDTO) {

        if (updateDTO.getAuthorId() != null) {
            UserEntity author = userRepository.findById(updateDTO.getAuthorId()).orElseThrow(() -> new UsernameNotFoundException("User with id: " + updateDTO.getAuthorId() + " was not found"));
            existing.setAuthor(author);
        } else if (existing.getAuthor() == null) {
            throw new IllegalStateException("Task author cannot be null");
        }
    }

    private void updateAndValidateStatus(TaskEntity existing, TaskUpdateDTO updateDTO) {
        if (updateDTO.getStatusId() != null) {
            TaskAttributeEntity status = taskAttributeRepository.findById(updateDTO.getStatusId()).orElseThrow(() -> new TaskAttributeNotFoundException("Status with id: " + updateDTO.getStatusId() + " was not found"));
            existing.setStatus(status);
        } else if (existing.getStatus() == null) {
            throw new IllegalStateException("Task status cannot be null");
        }
    }

    private void updateAndValidatePriority(TaskEntity existing, TaskUpdateDTO updateDTO) {
        if (updateDTO.getPriorityId() != null) {
            TaskAttributeEntity priority = taskAttributeRepository.findById(updateDTO.getPriorityId()).orElseThrow(() -> new TaskAttributeNotFoundException("Priority with id: " + updateDTO.getPriorityId() + " was not found"));
            existing.setPriority(priority);
        } else if (existing.getPriority() == null) {
            throw new IllegalStateException("Task priority cannot be null");
        }
    }

    private void updateParentTask(TaskEntity existing, TaskUpdateDTO updateDTO) {

        if (updateDTO.getParentTaskId() != null) {
            TaskEntity parentTask = taskRepository.findById(updateDTO.getParentTaskId()).orElseThrow(() -> new TaskNotFoundException("Parent task with id: " + updateDTO.getParentTaskId() + " was not found"));
            existing.setParentTask(parentTask);
        }
    }

    @Transactional(readOnly = true)
    public List<TaskCommentDTO> getAllComments(Long taskId) {
        log.info("getAllComments – fetching comments for taskId={}", taskId);
        List<TaskCommentDTO> dtos = taskCommentRepository.findAllByTaskId(taskId).stream().map(commentMapper::toDTO).toList();
        log.debug("getAllComments – returned {} comments for taskId={}", dtos.size(), taskId);
        return dtos;
    }

    @Transactional(readOnly = true)
    public List<TaskCommentDTO> getRootComments(Long taskId) {
        log.info("getRootComments – fetching root comments for taskId={}", taskId);
        List<TaskCommentDTO> dtos = taskCommentRepository.findByTaskIdAndParentCommentIsNull(taskId).stream().map(commentMapper::toDTO).toList();
        log.debug("getRootComments – returned {} root comments for taskId={}", dtos.size(), taskId);
        return dtos;
    }

    @Transactional(readOnly = true)
    public List<TaskCommentDTO> getChildComments(Long taskId, Long parentCommentId) {
        log.info("getChildComments – fetching child comments for parentCommentId={} on taskId={}", parentCommentId, taskId);
        taskRepository.findById(taskId).orElseThrow(() -> {
            log.error("getChildComments – task not found: {}", taskId);
            return new TaskNotFoundException("Task not found: " + taskId);
        });
        List<TaskCommentDTO> dtos = taskCommentRepository.findByTaskIdAndParentComment_CommentId(taskId, parentCommentId).stream().map(commentMapper::toDTO).toList();
        log.debug("getChildComments – returned {} child comments for parentCommentId={}", dtos.size(), parentCommentId);
        return dtos;
    }

    @Transactional
    public void changePriority(Long taskId, Long priorityId) {
        log.info("changePriority – delegating to aggregate service: taskId={}, priorityId={}", taskId, priorityId);
        taskAggregateService.changePriority(taskId, priorityId);
    }

    @Transactional
    public void changeStatus(Long taskId, Long statusId) {
        log.info("changeStatus – delegating to aggregate service: taskId={}, statusId={}", taskId, statusId);
        taskAggregateService.changeStatus(taskId, statusId);
    }

    @Transactional
    public void assignUserToTask(Long taskId, Long userId) {
        log.info("assignUserToTask – start: taskId={}, userId={}", taskId, userId);

        taskAggregateService.assignUser(taskId, userId);

        TaskEntity task = taskRepository.findById(taskId)
                .orElseThrow(() -> new TaskNotFoundException("Task not found: " + taskId));
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + userId));

        String message = "You have been assigned to task: \"" + task.getTitle() + "\"";
        NotificationEntity notif = notificationService.createInAppNotification(user, message);
        log.info("assignUserToTask – in-app notification created for userId={}", userId);

        emailNotificationService.sendTaskAssignmentEmail(user.getEmail(), task);
        log.info("assignUserToTask – email notification sent to {}", user.getEmail());

        webSocketNotificationService.sendInAppNotification(notif);
        log.info("assignUserToTask – WebSocket notification sent to {}", user.getUsername());

        log.info("assignUserToTask – completed: taskId={}, userId={}", taskId, userId);
    }


    @Transactional
    public void removeUserFromTask(Long taskId, Long userId) {
        log.info("removeUserFromTask – delegating to aggregate service: taskId={}, userId={}", taskId, userId);
        taskAggregateService.removeAssignedUser(taskId, userId);
    }

    @Transactional
    public void assignTag(Long taskId, Long tagId) {
        log.info("assignTag – delegating to aggregate service: taskId={}, tagId={}", taskId, tagId);
        taskAggregateService.assignTag(taskId, tagId);
    }

    @Transactional
    public void removeTagFromTask(Long taskId, Long tagId) {
        log.info("removeTagFromTask – delegating to aggregate service: taskId={}, tagId={}", taskId, tagId);
        taskAggregateService.unassignTag(taskId, tagId);
    }

    public List<TagDTO> getTags(Long taskId) {
        log.info("getTags – fetching tags for taskId={}", taskId);
        List<TagDTO> dtos = taskAggregateService.getTags(taskId);
        log.debug("getTags – returned {} tags for taskId={}", dtos.size(), taskId);
        return dtos;
    }

    public TaskDTO getParentTask(Long id) {
        log.info("getParentTask – fetching parent for taskId={}", id);
        TaskEntity task = taskRepository.findById(id).orElseThrow(() -> {
            log.error("getParentTask – task not found: {}", id);
            return new TaskNotFoundException("task with id: " + id + " was not found");
        });
        if (task.getParentTask() == null) {
            log.warn("getParentTask – no parent for taskId={}", id);
            throw new TaskNotFoundException("task doesn't have a parent");
        }
        TaskDTO dto = taskMapper.toDTO(task.getParentTask());
        log.debug("getParentTask – returning parent taskId={}", dto.getId());
        return dto;
    }
}
