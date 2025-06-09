package app.task_manager.taskAttribute.exception;

public class TaskAttributeNotFoundException extends RuntimeException {
    public TaskAttributeNotFoundException(String message) {
        super(message);
    }
}
