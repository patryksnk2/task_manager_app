package app.task_manager.task;

import app.task_manager.User.UserAlreadyAssignedException;
import app.task_manager.User.UserEntity;
import app.task_manager.User.UserNotAssignException;
import app.task_manager.tag.TagAlreadyExistException;
import app.task_manager.tag.TagEntity;
import app.task_manager.tag.TagNotFoundException;
import app.task_manager.task_comment.TaskCommentDTO;
import app.task_manager.task_comment.TaskCommentEntity;
import app.task_manager.task_comment.TaskCommentNotFoundException;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class TaskAggregate {
    private TaskEntity taskEntity;
    @Builder.Default
    private List<TaskCommentEntity> comments = new ArrayList<>();
    @Builder.Default
    private List<UserEntity> assignedUsers = new ArrayList<>();
    @Builder.Default
    private List<TagEntity> tags = new ArrayList<>();

    public void addComment(TaskCommentDTO taskCommentDTO, UserEntity user) {
        if (taskCommentDTO == null) {
            throw new IllegalArgumentException("Comment DTO cannot be null.");
        }
        if (user == null) {
            throw new IllegalArgumentException("User cannot be null.");
        }

        TaskCommentEntity newComment = TaskCommentEntity.builder()
                .task(taskEntity)
                .user(user)
                .content(taskCommentDTO.getContent())
                .build();

        comments.add(newComment);
    }

    public void assignUser(UserEntity user) {
        if (user == null) {
            throw new IllegalArgumentException("user cannot be null");
        }
        if (assignedUsers.stream().anyMatch(u -> u.getUserId().equals(user.getUserId()))) {
            throw new UserAlreadyAssignedException("User is already assign to this task");
        }
        assignedUsers.add(user);
    }

    public void removeComment(Long commentId) {
        if (commentId == null) {
            throw new IllegalArgumentException("commentId cannot be null");
        }
        boolean removedComment = comments.removeIf(comment -> {
            if (comment.getCommentId().equals(commentId)) {
                comment.setUser(null);
                comment.setTask(null);
                return true;
            }
            return false;
        });
        if (!removedComment) {
            throw new TaskCommentNotFoundException("comment with id:" + commentId + "not found in this task");
        }
    }

    public void removeAssignedUser(Long userId) {
        if (userId == null) {
            throw new IllegalArgumentException("userId cannot be null");
        }
        if (assignedUsers.removeIf(user -> user.getUserId().equals(userId))) return;
        throw new UserNotAssignException("User with id " + userId + "is not assigned to this task");
    }

    public void addTag(TagEntity tag) {
        if (tag == null) {
            throw new IllegalArgumentException("tag cannot be null");
        }
        if (tags.stream().anyMatch(t -> t.getTag_id().equals(tag.getTag_id()))) {
            throw new TagAlreadyExistException("this tag is elareday assigned to this task");
        }
    }

    public void removeTag(Long tagId) {
        if (tagId == null) {
            throw new IllegalArgumentException("tagID cannot be null");
        }
        if (tags.removeIf(tag -> tag.getTag_id().equals(tagId))) return;
        throw new TagNotFoundException("tag with id:" + tagId + "not found in this task");
    }
}
