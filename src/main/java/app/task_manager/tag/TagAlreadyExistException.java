package app.task_manager.tag;

public class TagAlreadyExistException extends RuntimeException {
    public TagAlreadyExistException(String message) {
        super(message);
    }
}
