package app.task_manager.task_comment.exception;

public class TaskCommentNotFoundException extends RuntimeException {
    public TaskCommentNotFoundException(String message) {
        super(message);
    }
}
