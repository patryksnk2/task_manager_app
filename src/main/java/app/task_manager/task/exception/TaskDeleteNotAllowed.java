package app.task_manager.task.exception;

public class TaskDeleteNotAllowed extends RuntimeException {
    public TaskDeleteNotAllowed(String message) {
        super(message);
    }
}
