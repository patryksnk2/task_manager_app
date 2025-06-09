package app.task_manager.tag.exception;

public class TagAlreadyExistException extends RuntimeException {
    public TagAlreadyExistException(String message) {
        super(message);
    }
}
