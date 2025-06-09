package app.task_manager.taskAttribute.exception;

public class SameAttributeException extends RuntimeException {
    public SameAttributeException(String message) {
        super(message);
    }
}
