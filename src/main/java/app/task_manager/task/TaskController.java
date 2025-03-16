package app.task_manager.task;

import org.springframework.http.HttpStatus;
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
//
//    @GetMapping
//    List<Task> findAll() {
//        return taskService.findAll();
//    }
//
//    @GetMapping("{id}")
//    Task findById(@PathVariable Long id) {
//        return taskService.findById(id);
//    }

//    @PostMapping("/create")
//    @ResponseStatus(HttpStatus.CREATED)
//    Task create(@RequestBody Task task) {
//        return taskService.create(task);
//    }
//
//    @PutMapping("/update/{id}")
//    Task update(@PathVariable Long id, @RequestBody Task task) {
//        return taskService.update(id, task);
//    }
//
//    @DeleteMapping("/delete/{id}")
//    void delete(@PathVariable Long id) {
//        taskService.deleteById(id);
//    }
}
