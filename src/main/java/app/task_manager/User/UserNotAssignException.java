package app.task_manager.User;

public class UserNotAssignException extends RuntimeException {
    public UserNotAssignException(String message) {
        super(message);
    }
}
