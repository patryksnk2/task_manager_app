package app.task_manager.task_comment;

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
