package app.task_manager.task_comment;

import jakarta.validation.Valid;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/task-comments")
public class TaskCommentController {

    private final TaskCommentService taskCommentService;

    public TaskCommentController(TaskCommentService taskCommentService) {
        this.taskCommentService = taskCommentService;
    }

    @PostMapping
    public ResponseEntity<TaskCommentDTO> createComment(@Valid @RequestBody TaskCommentDTO taskCommentDTO) {
        TaskCommentDTO createdComment = taskCommentService.create(taskCommentDTO);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Location", "/api/task-comments/" + createdComment.getCommentId());

        return new ResponseEntity<>(createdComment, headers, HttpStatus.CREATED);
    }


    @GetMapping
    public ResponseEntity<List<TaskCommentDTO>> getAllComments() {
        List<TaskCommentDTO> taskComments = taskCommentService.findAll();

        HttpHeaders headers = new HttpHeaders();
        headers.add("X-Total-Count", String.valueOf(taskComments.size()));

        return new ResponseEntity<>(taskComments, headers, HttpStatus.OK);
    }


    @GetMapping("/{id}")
    public ResponseEntity<TaskCommentDTO> getCommentById(@PathVariable Long id) {
        TaskCommentDTO taskCommentDTO = taskCommentService.findById(id);

        HttpHeaders headers = new HttpHeaders();
        headers.add("X-Request-ID", id.toString());

        return new ResponseEntity<>(taskCommentDTO, headers, HttpStatus.OK);
    }


    @PutMapping("/{id}")
    public ResponseEntity<TaskCommentDTO> updateComment(@PathVariable Long id,
                                                        @Valid @RequestBody TaskCommentDTO taskCommentDTO) {
        TaskCommentDTO updatedComment = taskCommentService.update(id, taskCommentDTO);

        HttpHeaders headers = new HttpHeaders();
        headers.add("X-Updated-At", updatedComment.getUpdatedAt().toString());

        return new ResponseEntity<>(updatedComment, headers, HttpStatus.OK);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteComment(@PathVariable Long id) {
        taskCommentService.delete(id);

        HttpHeaders headers = new HttpHeaders();
        headers.add("X-Deleted-At", java.time.LocalDateTime.now().toString());

        return new ResponseEntity<>(headers, HttpStatus.NO_CONTENT);
    }
}
