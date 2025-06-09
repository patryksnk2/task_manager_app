package app.task_manager.task_comment.aggregate;

import app.task_manager.User.entity.UserEntity;
import app.task_manager.User.repository.UserRepository;
import app.task_manager.task.entity.TaskEntity;
import app.task_manager.task.exception.TaskNotFoundException;
import app.task_manager.task.repository.TaskRepository;
import app.task_manager.task_comment.entity.TaskCommentEntity;
import app.task_manager.task_comment.exception.TaskCommentNotFoundException;
import app.task_manager.task_comment.exception.TaskCommentPermissionException;
import app.task_manager.task_comment.repository.TaskCommentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class TaskCommentAggregateService {

    private final TaskRepository taskRepository;
    private final TaskCommentRepository commentRepository;
    private final UserRepository userRepository;

    @Transactional
    public TaskCommentEntity addComment(Long taskId, String content, Long parentCommentId) {
        log.info("addComment – start: taskId={}, parentCommentId={}", taskId, parentCommentId);
        TaskEntity task = taskRepository.findById(taskId).orElseThrow(() -> {
            log.error("addComment – task not found: {}", taskId);
            return new TaskNotFoundException("Task not found with ID: " + taskId);
        });

        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        log.debug("addComment – current user: {}", username);
        UserEntity user = userRepository.findByUsername(username).orElseThrow(() -> {
            log.error("addComment – user not found: {}", username);
            return new UsernameNotFoundException("User with username: " + username + " was not found");
        });

        TaskCommentEntity parentComment = null;
        if (parentCommentId != null) {
            log.debug("addComment – loading parent comment: {}", parentCommentId);
            parentComment = commentRepository.findById(parentCommentId).orElseThrow(() -> {
                log.error("addComment – parent comment not found: {}", parentCommentId);
                return new TaskCommentNotFoundException("No parent comment with id: " + parentCommentId);
            });
        }

        TaskCommentAggregate aggregate = TaskCommentAggregate.builder().task(task).build();
        TaskCommentEntity comment = aggregate.addComment(user, content, parentComment);
        TaskCommentEntity saved = commentRepository.save(comment);
        log.info("addComment – saved commentId={} for taskId={}", saved.getCommentId(), taskId);
        return saved;
    }

    @Transactional
    public void removeComment(Long taskId, Long commentId) {
        log.info("removeComment – start: taskId={}, commentId={}", taskId, commentId);
        TaskEntity task = taskRepository.findById(taskId).orElseThrow(() -> {
            log.error("removeComment – task not found: {}", taskId);
            return new TaskNotFoundException("Task not found: " + taskId);
        });

        TaskCommentEntity comment = commentRepository.findById(commentId).orElseThrow(() -> {
            log.error("removeComment – comment not found: {}", commentId);
            return new TaskCommentNotFoundException("Comment not found with ID: " + commentId);
        });

        if (!comment.getTask().getId().equals(taskId)) {
            log.warn("removeComment – commentId={} does not belong to taskId={}", commentId, taskId);
            throw new IllegalArgumentException("Comment " + commentId + " does not belong to task " + taskId);
        }

        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        log.debug("removeComment – current user: {}", username);
        UserEntity currentUser = userRepository.findByUsername(username).orElseThrow(() -> {
            log.error("removeComment – user not found: {}", username);
            return new UsernameNotFoundException("User not found: " + username);
        });

        TaskCommentAggregate aggregate = new TaskCommentAggregate(task);
        aggregate.removeComment(commentId, currentUser);
        commentRepository.deleteById(commentId);
        log.info("removeComment – commentId={} deleted from taskId={}", commentId, taskId);
    }

    @Transactional
    public TaskCommentEntity editComment(Long taskId, Long commentId, String newContent) {
        log.info("editComment – start: taskId={}, commentId={}", taskId, commentId);
        TaskEntity task = taskRepository.findById(taskId).orElseThrow(() -> {
            log.error("editComment – task not found: {}", taskId);
            return new TaskNotFoundException("Task not found: " + taskId);
        });

        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        log.debug("editComment – current user: {}", username);
        UserEntity user = userRepository.findByUsername(username).orElseThrow(() -> {
            log.error("editComment – user not found: {}", username);
            return new UsernameNotFoundException("User not found: " + username);
        });

        TaskCommentAggregate aggregate = new TaskCommentAggregate(task);
        TaskCommentEntity updated = aggregate.editComment(commentId, newContent, user);
        log.info("editComment – commentId={} updated on taskId={}", commentId, taskId);
        return updated;
    }
}
