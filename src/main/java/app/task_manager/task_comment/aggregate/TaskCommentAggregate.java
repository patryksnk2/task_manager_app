package app.task_manager.task_comment.aggregate;

import app.task_manager.User.entity.UserEntity;
import app.task_manager.task.entity.TaskEntity;
import app.task_manager.task_comment.entity.TaskCommentEntity;
import app.task_manager.task_comment.exception.TaskCommentNotFoundException;
import app.task_manager.task_comment.exception.TaskCommentPermissionException;
import jakarta.annotation.Nullable;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@Builder
@Slf4j
public class TaskCommentAggregate {

    private final @NonNull TaskEntity task;

    public List<TaskCommentEntity> getComments() {
        log.debug("Retrieving all comments for taskId={}", task.getId());
        return task.getComments();
    }

    public TaskCommentEntity addComment(@Valid UserEntity user, @NotBlank String content, @Nullable TaskCommentEntity parentComment) {
        Long userId = user.getUserId();
        log.info("Attempting to add comment to taskId={} by userId={}", task.getId(), userId);

        if (!task.getAssignedUsers().contains(user)) {
            log.warn("UserId={} is not assigned to taskId={}, cannot add comment", userId, task.getId());
            throw new TaskCommentPermissionException("User not assigned to task");
        }

        TaskCommentEntity newComment = TaskCommentEntity.builder().task(task).user(user).content(content.trim()).parentComment(parentComment).build();

        task.getComments().add(newComment);
        log.info("CommentId={} added to taskId={} by userId={}", newComment.getCommentId(), task.getId(), userId);
        return newComment;
    }

    public void removeComment(@NonNull Long commentId, @NonNull UserEntity user) {
        log.info("Attempting to remove commentId={} from taskId={} by userId={}", commentId, task.getId(), user.getUserId());

        TaskCommentEntity comment = task.getComments().stream().filter(c -> c.getCommentId().equals(commentId)).findFirst().orElseThrow(() -> {
            log.error("CommentId={} not found in taskId={}", commentId, task.getId());
            return new TaskCommentNotFoundException("Comment not found");
        });

        boolean isTaskAuthor = task.getAuthor().equals(user);
        boolean isCommentAuthor = comment.getUser().equals(user);
        if (!isTaskAuthor && !isCommentAuthor) {
            log.warn("UserId={} has no permission to remove commentId={} on taskId={}", user.getUserId(), commentId, task.getId());
            throw new TaskCommentPermissionException("User cannot delete this comment");
        }

        task.getComments().remove(comment);
        log.info("CommentId={} removed from taskId={} by userId={}", commentId, task.getId(), user.getUserId());
    }

    public List<TaskCommentEntity> getRootComments() {
        log.debug("Retrieving root comments for taskId={}", task.getId());
        List<TaskCommentEntity> roots = task.getComments().stream().filter(c -> c.getParentComment() == null).collect(Collectors.toList());
        log.debug("Found {} root comments for taskId={}", roots.size(), task.getId());
        return roots;
    }

    public TaskCommentEntity editComment(@NonNull Long commentId, @NotBlank String newContent, @NonNull UserEntity user) {
        Long userId = user.getUserId();
        log.info("Attempting to edit commentId={} on taskId={} by userId={}", commentId, task.getId(), userId);

        TaskCommentEntity comment = task.getComments().stream().filter(c -> c.getCommentId().equals(commentId)).findFirst().orElseThrow(() -> {
            log.error("CommentId={} not found in taskId={}", commentId, task.getId());
            return new TaskCommentNotFoundException("Comment not found");
        });

        if (!comment.getUser().equals(user)) {
            log.warn("UserId={} has no permission to edit commentId={} on taskId={}", userId, commentId, task.getId());
            throw new TaskCommentPermissionException("User cannot edit this comment");
        }

        comment.setContent(newContent.trim());
        log.info("CommentId={} on taskId={} edited by userId={}", commentId, task.getId(), userId);
        return comment;
    }
}
