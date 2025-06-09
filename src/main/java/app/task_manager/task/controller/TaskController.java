package app.task_manager.task.controller;

import app.task_manager.tag.dto.TagDTO;
import app.task_manager.tag.entity.TagEntity;
import app.task_manager.task.dto.TaskCreatorDTO;
import app.task_manager.task.dto.TaskDTO;
import app.task_manager.task.dto.TaskUpdateDTO;
import app.task_manager.task.entity.TaskEntity;
import app.task_manager.task.service.TaskService;
import app.task_manager.task_comment.dto.TaskCommentDTO;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/tasks")
@AllArgsConstructor
@Slf4j
public class TaskController {
    private final TaskService taskService;

    @GetMapping
    public ResponseEntity<List<TaskDTO>> findAll() {
        log.info("GET /api/tasks – fetching all tasks");
        List<TaskDTO> tasks = taskService.findAll();
        log.info("GET /api/tasks – returned {} tasks", tasks.size());

        HttpHeaders headers = new HttpHeaders();
        headers.add("X-Total-Count", String.valueOf(tasks.size()));
        return ResponseEntity.ok().headers(headers).body(tasks);
    }


    @PostMapping
    public ResponseEntity<TaskDTO> create(@Valid @RequestBody TaskCreatorDTO creatorDTO) {
        log.info("POST /api/tasks – create task request");
        TaskDTO created = taskService.create(creatorDTO);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(created.getId()).toUri();
        log.info("POST /api/tasks – task created with id={}, location={}", created.getId(), location);

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.LOCATION, location.toString());
        return ResponseEntity.created(location).headers(headers).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TaskDTO> update(@PathVariable Long id, @Valid @RequestBody TaskUpdateDTO taskDTO) {
        log.info("PUT /api/tasks/{} – update task request", id);
        TaskDTO updatedTask = taskService.update(id, taskDTO);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().build().toUri();
        log.info("PUT /api/tasks/{} – task updated, location={}", updatedTask.getId(), location);

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.LOCATION, location.toString());
        return ResponseEntity.ok().headers(headers).body(updatedTask);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TaskDTO> findById(@PathVariable Long id) {
        log.info("GET /api/tasks/{} – fetch task by id", id);
        TaskDTO task = taskService.findById(id);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().build().toUri();
        log.info("GET /api/tasks/{} – returning task, location={}", id, location);

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.LOCATION, location.toString());
        return ResponseEntity.ok().headers(headers).body(task);
    }

    @PostMapping("/{id}/priority/{priorityId}")
    public ResponseEntity<Void> changePriority(@PathVariable Long id, @PathVariable Long priorityId) {
        log.info("POST /api/tasks/{}/priority/{} – change priority request", id, priorityId);
        taskService.changePriority(id, priorityId);
        log.info("POST /api/tasks/{}/priority/{} – priority changed", id, priorityId);
        return ResponseEntity.noContent().build();
    }


    @PostMapping("/{id}/status/{statusId}")
    public ResponseEntity<Void> changeStatus(@PathVariable Long id, @PathVariable Long statusId) {
        log.info("POST /api/tasks/{}/status/{} – change status request", id, statusId);
        taskService.changeStatus(id, statusId);
        log.info("POST /api/tasks/{}/status/{} – status changed", id, statusId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        log.info("DELETE /api/tasks/{} – delete task request", id);
        taskService.delete(id);
        log.info("DELETE /api/tasks/{} – task deleted", id);
        return ResponseEntity.status(410).build();
    }

    @PostMapping("/{id}/assigned-users/{userId}")
    public ResponseEntity<Void> assignUserToTask(@PathVariable("id") Long taskId, @PathVariable("userId") Long userId) {
        log.info("POST /api/tasks/{}/assigned-users/{} – assign user request", taskId, userId);
        taskService.assignUserToTask(taskId, userId);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().build().toUri();
        log.info("POST /api/tasks/{}/assigned-users/{} – user assigned, location={}", taskId, userId, location);
        return ResponseEntity.created(location).build();
    }

    @DeleteMapping("/{id}/assigned-users/{userId}")
    public ResponseEntity<Void> removeUserFromTask(@PathVariable("id") Long taskId, @PathVariable("userId") Long userId) {
        log.info("DELETE /api/tasks/{}/assigned-users/{} – remove user request", taskId, userId);
        taskService.removeUserFromTask(taskId, userId);
        log.info("DELETE /api/tasks/{}/assigned-users/{} – user removed", taskId, userId);
        return ResponseEntity.status(410).build();
    }


    @PostMapping("/{id}/tags/{tagId}")
    public ResponseEntity<Void> assignTag(@PathVariable("id") Long taskId, @PathVariable("tagId") Long tagId) {
        log.info("POST /api/tasks/{}/tags/{} – assign tag request", taskId, tagId);
        taskService.assignTag(taskId, tagId);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().build().toUri();
        log.info("POST /api/tasks/{}/tags/{} – tag assigned, location={}", taskId, tagId, location);
        return ResponseEntity.created(location).build();
    }

    @DeleteMapping("/{id}/tags/{tagId}")
    public ResponseEntity<Void> removeTagFromTask(@PathVariable("id") Long taskId, @PathVariable("tagId") Long tagId) {
        log.info("DELETE /api/tasks/{}/tags/{} – remove tag request", taskId, tagId);
        taskService.removeTagFromTask(taskId, tagId);
        log.info("DELETE /api/tasks/{}/tags/{} – tag removed", taskId, tagId);
        return ResponseEntity.status(410).build();
    }

    @GetMapping("/{id}/tags")
    public ResponseEntity<List<TagDTO>> getTags(@PathVariable Long id) {
        log.info("GET /api/tasks/{}/tags – fetching tags", id);
        List<TagDTO> tags = taskService.getTags(id);
        log.info("GET /api/tasks/{}/tags – returned {} tags", id, tags.size());
        return ResponseEntity.ok().body(tags);
    }


    @GetMapping("/{id}/parent")
    public ResponseEntity<TaskDTO> getParentTask(@PathVariable Long id) {
        log.info("GET /api/tasks/{}/parent – fetching parent task", id);
        TaskDTO parentTask = taskService.getParentTask(id);
        log.info("GET /api/tasks/{}/parent – returning parent taskId={}", id, parentTask.getId());
        return ResponseEntity.ok().body(parentTask);
    }

    @GetMapping("/{id}/comments")
    public ResponseEntity<List<TaskCommentDTO>> getAllComments(@PathVariable("id") Long taskId) {
        log.info("GET /api/tasks/{}/comments – fetching all comments", taskId);
        List<TaskCommentDTO> comments = taskService.getAllComments(taskId);
        log.info("GET /api/tasks/{}/comments – returned {} comments", taskId, comments.size());
        return ResponseEntity.ok(comments);
    }

    @GetMapping("/{id}/comments/root")
    public ResponseEntity<List<TaskCommentDTO>> getRootComments(@PathVariable("id") Long taskId) {
        log.info("GET /api/tasks/{}/comments/root – fetching root comments", taskId);
        List<TaskCommentDTO> rootComments = taskService.getRootComments(taskId);
        log.info("GET /api/tasks/{}/comments/root – returned {} root comments", taskId, rootComments.size());
        return ResponseEntity.ok(rootComments);
    }

    @GetMapping("/{id}/comments/{parentCommentId}/children")
    public ResponseEntity<List<TaskCommentDTO>> getChildComments(@PathVariable("id") Long taskId, @PathVariable Long parentCommentId) {
        log.info("GET /api/tasks/{}/comments/{}/children – fetching child comments", taskId, parentCommentId);
        List<TaskCommentDTO> childComments = taskService.getChildComments(taskId, parentCommentId);
        log.info("GET /api/tasks/{}/comments/{}/children – returned {} child comments", taskId, parentCommentId, childComments.size());
        return ResponseEntity.ok(childComments);
    }


}
