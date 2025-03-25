package app.task_manager.task;

import jakarta.validation.Valid;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {
    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping
    public ResponseEntity<List<TaskDTO>> findAll() {
        List<TaskDTO> tasks = taskService.findAll();

        HttpHeaders headers = new HttpHeaders();
        headers.add("X-Total-Count", String.valueOf(tasks.size()));  // Dodanie nagłówka X-Total-Count, który informuje o liczbie zadań.

        return ResponseEntity.ok()
                .headers(headers)
                .body(tasks);
    }


    @PostMapping
    public ResponseEntity<TaskDTO> create(@Valid @RequestBody TaskDTO taskDTO) {
        TaskDTO createdTask = taskService.create(taskDTO);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Location", "/api/tasks/" + createdTask.getId());  // Nagłówek Location wskazuje URL nowo utworzonego zasobu.

        return ResponseEntity.status(HttpStatus.CREATED)  // Kod statusu 201 - Created
                .headers(headers)
                .body(createdTask);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TaskDTO> update(@PathVariable Long id, @Valid @RequestBody TaskDTO taskDTO) {
        TaskDTO updatedTask = taskService.update(id, taskDTO);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Location", "/api/tasks/" + updatedTask.getId());
        return ResponseEntity.ok()
                .headers(headers)
                .body(updatedTask);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TaskDTO> findById(@PathVariable Long id) {
        TaskDTO task = taskService.findById(id);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Location", "/api/tasks/" + task.getId());
        return ResponseEntity.ok()
                .headers(headers)
                .body(task);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        taskService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

}
