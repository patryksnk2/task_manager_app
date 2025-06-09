package app.task_manager.task_comment.mapper;

import app.task_manager.task_comment.repository.TaskCommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TaskCommentMapperHelper {
    private final TaskCommentRepository taskCommentRepository;

    @Autowired
    public TaskCommentMapperHelper(TaskCommentRepository taskCommentRepository) {
        this.taskCommentRepository = taskCommentRepository;
    }
}
