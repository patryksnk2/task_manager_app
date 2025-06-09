package app.task_manager.task.aggregate;

import app.task_manager.User.exception.UserAlreadyAssignedException;
import app.task_manager.User.entity.UserEntity;
import app.task_manager.User.exception.UserNotAssignException;
import app.task_manager.tag.exception.TagAlreadyExistException;
import app.task_manager.tag.entity.TagEntity;
import app.task_manager.tag.exception.TagNotFoundException;
import app.task_manager.task.entity.TaskEntity;
import app.task_manager.taskAttribute.entity.TaskAttributeEntity;
import app.task_manager.taskAttribute.exception.SameAttributeException;
import app.task_manager.taskAttribute.exception.TaskAttributeInUseException;
import app.task_manager.task_comment.entity.TaskCommentEntity;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Slf4j
public class TaskAggregate {

    @NonNull
    private TaskEntity taskEntity;

    public List<TaskCommentEntity> getComments() {
        log.debug("Retrieving all comments for taskId={}", taskEntity.getId());
        return List.copyOf(taskEntity.getComments());
    }

    public List<UserEntity> getAssignedUsers() {
        log.debug("Retrieving assigned users for taskId={}", taskEntity.getId());
        return List.copyOf(taskEntity.getAssignedUsers());
    }

    public List<TagEntity> getTags() {
        log.debug("Retrieving tags for taskId={}", taskEntity.getId());
        return List.copyOf(taskEntity.getTags());
    }

    public void assignUser(@NonNull UserEntity user) {
        Long userId = user.getUserId();
        log.info("Attempting to assign userId={} to taskId={}", userId, taskEntity.getId());
        if (isUserAssigned(user)) {
            log.warn("UserId={} is already assigned to taskId={}", userId, taskEntity.getId());
            throw new UserAlreadyAssignedException("User is already assigned to this task.");
        }
        taskEntity.getAssignedUsers().add(user);
        log.info("UserId={} assigned to taskId={}", userId, taskEntity.getId());
    }

    public void removeAssignedUser(@NonNull Long userId) {
        log.info("Attempting to remove assigned userId={} from taskId={}", userId, taskEntity.getId());
        if (taskEntity.getAuthor() != null && taskEntity.getAuthor().getUserId().equals(userId)) {
            log.warn("Cannot remove author userId={} from taskId={}", userId, taskEntity.getId());
            throw new IllegalArgumentException("Cannot remove the author from assigned users.");
        }
        boolean userRemoved = taskEntity.getAssignedUsers().removeIf(user -> user.getUserId().equals(userId));
        if (!userRemoved) {
            log.warn("UserId={} was not assigned to taskId={}", userId, taskEntity.getId());
            throw new UserNotAssignException("User with ID " + userId + " is not assigned to this task.");
        }
        log.info("UserId={} successfully removed from taskId={}", userId, taskEntity.getId());
    }

    public void changePriority(@NonNull TaskAttributeEntity priority)  {
        log.info("Changing priority for taskId={} from '{}' to '{}'", taskEntity.getId(), taskEntity.getPriority().getName(), priority.getName());
        if (taskEntity.getPriority().equals(priority)) {
            log.warn("Priority '{}' is already set on taskId={}", priority.getName(), taskEntity.getId());
            throw new SameAttributeException("Priority is already set to " + priority.getName());
        }
        taskEntity.setPriority(priority);
        log.info("Priority changed to '{}' for taskId={}", priority.getName(), taskEntity.getId());
    }

    public void changeStatus(@NonNull TaskAttributeEntity status) {
        log.info("Changing status for taskId={} from '{}' to '{}'", taskEntity.getId(), taskEntity.getStatus().getName(), status.getName());
        if (taskEntity.getStatus().equals(status)) {
            log.warn("Status '{}' is already set on taskId={}", status.getName(), taskEntity.getId());
            throw new SameAttributeException("Status is already set to " + status.getName());
        }
        taskEntity.setStatus(status);
        log.info("Status changed to '{}' for taskId={}", status.getName(), taskEntity.getId());
    }

    public void assignTag(@NonNull TagEntity tag) {
        Long tagId = tag.getTag_id();
        log.info("Attempting to assign tagId={} to taskId={}", tagId, taskEntity.getId());
        if (taskEntity.getTags().stream().anyMatch(t -> t.getTag_id().equals(tagId))) {
            log.warn("TagId={} is already assigned to taskId={}", tagId, taskEntity.getId());
            throw new TagAlreadyExistException("This tag is already assigned to this task.");
        }
        taskEntity.getTags().add(tag);
        log.info("TagId={} assigned to taskId={}", tagId, taskEntity.getId());
    }

    public void unassignTag(@NonNull Long tagId) {
        log.info("Attempting to unassign tagId={} from taskId={}", tagId, taskEntity.getId());
        boolean tagRemoved = taskEntity.getTags().removeIf(tag -> tag.getTag_id().equals(tagId));
        if (!tagRemoved) {
            log.warn("TagId={} not found on taskId={}", tagId, taskEntity.getId());
            throw new TagNotFoundException("Tag with ID: " + tagId + " not found in this task.");
        }
        log.info("TagId={} unassigned from taskId={}", tagId, taskEntity.getId());
    }

    private boolean isUserAssigned(@NonNull UserEntity user) {
        boolean assigned = taskEntity.getAssignedUsers().stream().anyMatch(u -> u.getUserId().equals(user.getUserId()));
        log.debug("isUserAssigned check for userId={} on taskId={} returned {}", user.getUserId(), taskEntity.getId(), assigned);
        return assigned;
    }

    public List<TaskCommentEntity> getRootComments() {
        log.debug("Retrieving root comments for taskId={}", taskEntity.getId());
        return taskEntity.getComments().stream().filter(c -> c.getParentComment() == null).collect(Collectors.toList());
    }

    public List<TaskCommentEntity> getChildComments(@NonNull TaskCommentEntity parentComment) {
        log.debug("Retrieving child comments of parentCommentId={} for taskId={}", parentComment.getCommentId(), taskEntity.getId());
        return taskEntity.getComments().stream().filter(c -> parentComment.equals(c.getParentComment())).collect(Collectors.toList());
    }
}
