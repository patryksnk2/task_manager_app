package app.task_manager.task_comment.controller;

import app.task_manager.task_comment.dto.TaskCommentCreateDTO;
import app.task_manager.task_comment.dto.TaskCommentDTO;
import app.task_manager.task_comment.dto.TaskCommentUpdateDTO;
import app.task_manager.task_comment.service.TaskCommentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/tasks/{taskId}/comments")
@RequiredArgsConstructor
@Slf4j
public class TaskCommentController {

    private final TaskCommentService commentService;

    @PostMapping
    public ResponseEntity<TaskCommentDTO> addComment(@PathVariable Long taskId, @Valid @RequestBody TaskCommentCreateDTO dto) {

        log.info("POST /api/tasks/{}/comments – add comment request: {}", taskId, dto);
        TaskCommentDTO created = commentService.create(taskId, dto);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(created.getCommentId()).toUri();
        log.info("POST /api/tasks/{}/comments – comment created with id={}, location={}", taskId, created.getCommentId(), location);

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.LOCATION, location.toString());

        return ResponseEntity.created(location).headers(headers).body(created);
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<Void> deleteComment(@PathVariable Long taskId, @PathVariable Long commentId) {

        log.info("DELETE /api/tasks/{}/comments/{} – delete comment request", taskId, commentId);
        commentService.delete(taskId, commentId);
        log.info("DELETE /api/tasks/{}/comments/{} – comment deleted", taskId, commentId);

        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{commentId}")
    public ResponseEntity<TaskCommentDTO> update(@PathVariable Long taskId, @PathVariable Long commentId, @Valid @RequestBody TaskCommentUpdateDTO dto) {

        log.info("PATCH /api/tasks/{}/comments/{} – update comment request: {}", taskId, commentId, dto);
        TaskCommentDTO result = commentService.update(taskId, commentId, dto);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest().build().toUri();
        log.info("PATCH /api/tasks/{}/comments/{} – comment updated, location={}", taskId, commentId, location);

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.LOCATION, location.toString());

        return ResponseEntity.ok().headers(headers).body(result);
    }

}
