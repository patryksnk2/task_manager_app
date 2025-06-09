package app.task_manager.taskAttribute.exception;

public class TaskAttributeInUseException extends RuntimeException {
    public TaskAttributeInUseException(String message) {
        super(message);
    }
}
