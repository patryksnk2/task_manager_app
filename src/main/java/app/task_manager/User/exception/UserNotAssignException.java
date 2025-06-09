package app.task_manager.User.exception;

public class UserNotAssignException extends RuntimeException {
    public UserNotAssignException(String message) {
        super(message);
    }
}
