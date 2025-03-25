package app.task_manager.taskAttribute;

import app.task_manager.task.TaskService;
import jakarta.validation.Valid;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/task_attribute")
public class TaskAttributeController {
    private final TaskAttributeService taskAttributeService;

    public TaskAttributeController(TaskAttributeService taskAttributeService) {
        this.taskAttributeService = taskAttributeService;
    }


    @PostMapping
    public ResponseEntity<TaskAttributeDTO> create(@Valid @RequestBody TaskAttributeDTO taskAttributeDTO) {
        TaskAttributeDTO createdTaskAttribute = taskAttributeService.create(taskAttributeDTO);
        HttpHeaders headers = new HttpHeaders();

        headers.add("Location", "/api/task-attributes/" + createdTaskAttribute.getId());

        return ResponseEntity.status(HttpStatus.CREATED)
                .headers(headers)
                .body(createdTaskAttribute);
    }


    @GetMapping
    public ResponseEntity<List<TaskAttributeDTO>> getAll() {
        List<TaskAttributeDTO> taskAttributes = taskAttributeService.getAll();
        return new ResponseEntity<>(taskAttributes, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TaskAttributeDTO> getById(@PathVariable Long id) {
        TaskAttributeDTO taskAttribute = taskAttributeService.getById(id);
        return new ResponseEntity<>(taskAttribute, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TaskAttributeDTO> update(@PathVariable Long id, @Valid @RequestBody TaskAttributeDTO taskAttributeDTO) {
        TaskAttributeDTO updatedTaskAttribute = taskAttributeService.update(id, taskAttributeDTO);
        return ResponseEntity.status(HttpStatus.OK).body(updatedTaskAttribute);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        taskAttributeService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
