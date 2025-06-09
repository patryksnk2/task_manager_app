package app.task_manager.task_comment.repository;

import app.task_manager.task_comment.entity.TaskCommentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;
@RepositoryRestResource(exported = false)
public interface TaskCommentRepository extends JpaRepository<TaskCommentEntity, Long> {

    List<TaskCommentEntity> findAllByTaskId(Long taskId);

    List<TaskCommentEntity> findByTaskIdAndParentCommentIsNull(Long taskId);

    List<TaskCommentEntity> findByTaskIdAndParentComment_CommentId(Long taskId, Long parentCommentId);
}
