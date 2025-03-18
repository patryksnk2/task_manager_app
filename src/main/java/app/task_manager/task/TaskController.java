package app.task_manager.task;

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
                .headers(headers)  // Ustawiamy nagłówki
                .body(tasks);  // Zwracamy ciało odpowiedzi
    }


    @PostMapping("/create")
    public ResponseEntity<TaskDTO> create(@RequestBody TaskDTO taskDTO) {
        TaskDTO createdTask = taskService.create(taskDTO);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Location", "/api/tasks/" + createdTask.getId());  // Nagłówek Location wskazuje URL nowo utworzonego zasobu.

        return ResponseEntity.status(HttpStatus.CREATED)  // Kod statusu 201 - Created
                .headers(headers)  // Ustawiamy nagłówki
                .body(createdTask);  // Zwracamy ciało odpowiedzi (nowo utworzone zadanie)
    }


}
