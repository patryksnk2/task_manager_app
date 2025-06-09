package app.task_manager;

import app.task_manager.User.exception.*;
import app.task_manager.auth.exception.InvalidJwtAuthenticationException;
import app.task_manager.roles.exception.RoleAlreadyExistException;
import app.task_manager.roles.exception.RoleNotFoundException;
import app.task_manager.tag.exception.TagAlreadyExistException;
import app.task_manager.tag.exception.TagNotFoundException;
import app.task_manager.task.exception.TaskDeleteNotAllowed;
import app.task_manager.task.exception.TaskNotFoundException;
import app.task_manager.taskAttribute.exception.SameAttributeException;
import app.task_manager.taskAttribute.exception.TaskAttributeInUseException;
import app.task_manager.taskAttribute.exception.TaskAttributeNotFoundException;
import app.task_manager.task_comment.exception.TaskCommentNotFoundException;
import app.task_manager.task_comment.exception.TaskCommentPermissionException;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.validation.BindException;

import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(TaskNotFoundException.class)
    public ResponseEntity<String> handleTaskNotFoundException(TaskNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    @ExceptionHandler(TaskAttributeNotFoundException.class)
    public ResponseEntity<String> handleTaskAttributeNotFoundException(TaskAttributeNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<String> handleUserNotFoundException(UserNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    @ExceptionHandler(EmailAlreadyExistsException.class)
    public ResponseEntity<String> handleEmailAlreadyExistsException(EmailAlreadyExistsException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage()); // 409 Conflict
    }

    @ExceptionHandler(SameAttributeException.class)
    public ResponseEntity<String> handleSameAttributeException(SameAttributeException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage()); // 409 Conflict
    }

    @ExceptionHandler(InvalidCredentialsException.class)
    public ResponseEntity<String> handleInvalidCredentialsException(InvalidCredentialsException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ex.getMessage()); // 401 Unauthorized
    }

    @ExceptionHandler(InvalidJwtAuthenticationException.class)
    public ResponseEntity<String> handleInvalidJwtAuthenticationException(InvalidJwtAuthenticationException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ex.getMessage()); // 401 Unauthorized
    }

    @ExceptionHandler(TagNotFoundException.class)
    public ResponseEntity<String> handleTagNotFoundException(TagNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    @ExceptionHandler(TaskCommentNotFoundException.class)
    public ResponseEntity<String> handleTaskCommentNotFoundException(TaskCommentNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    @ExceptionHandler(UserAlreadyAssignedException.class)
    public ResponseEntity<String> handleUserAlreadyAssignedException(UserAlreadyAssignedException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage());
    }

    @ExceptionHandler(UserNotAssignException.class)
    public ResponseEntity<String> handleUserNotAssignException(UserNotAssignException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage());
    }

    @ExceptionHandler(TagAlreadyExistException.class)
    public ResponseEntity<String> handleTagAlreadyExistException(TagAlreadyExistException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage());
    }

    @ExceptionHandler(BindException.class)
    public ResponseEntity<String> handleValidationException(BindException ex) {
        String errorMessages = ex.getAllErrors().stream().map(DefaultMessageSourceResolvable::getDefaultMessage).collect(Collectors.joining(", "));
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Validation error(s): " + errorMessages);
    }

    @ExceptionHandler(RoleAlreadyExistException.class)
    public ResponseEntity<String> handleRoleAlreadyExistException(RoleAlreadyExistException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage());
    }

    @ExceptionHandler(RoleNotFoundException.class)
    public ResponseEntity<String> handleRoleNotFoundException(RoleNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    @ExceptionHandler(TaskCommentPermissionException.class)
    public ResponseEntity<String> handleTaskCommentPermissionException(TaskCommentPermissionException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ex.getMessage());
    }

    @ExceptionHandler(TaskAttributeInUseException.class)
    public ResponseEntity<String> handleTaskAttributeInUseException(TaskAttributeInUseException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage());
    }

    @ExceptionHandler(TaskDeleteNotAllowed.class)
    public ResponseEntity<String> handleTaskDeleteNotAllowed(TaskDeleteNotAllowed ex) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ex.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<String> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        String errorMessages = ex.getBindingResult().getAllErrors().stream().map(DefaultMessageSourceResolvable::getDefaultMessage).collect(Collectors.joining(", "));
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Validation error(s): " + errorMessages);
    }
}
