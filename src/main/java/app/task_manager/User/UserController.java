package app.task_manager.User;

import jakarta.validation.Valid;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<UserDTO> create(@Valid @RequestBody UserDTO userDTO) {
        UserDTO createdUser = userService.create(userDTO);

        HttpHeaders headers = new HttpHeaders();
        headers.add("X-User-ID", String.valueOf(createdUser.getUserId()));

        return ResponseEntity.status(HttpStatus.CREATED)
                .headers(headers)
                .body(createdUser);
    }

    @GetMapping("/login")
    public ResponseEntity<UserDTO> login(@Valid @RequestBody UserDTO userDTO) {
        UserDTO user = userService.login(userDTO);
        return ResponseEntity
                .ok()
                .body(user);
    }

    @GetMapping
    public ResponseEntity<List<UserDTO>> getAll() {
        List<UserDTO> users = userService.getAll();

        HttpHeaders headers = new HttpHeaders();
        headers.add("X-Total-Users", String.valueOf(users.size()));

        return ResponseEntity.ok()
                .headers(headers)
                .body(users);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserDTO> getById(@PathVariable Long userId) {
        UserDTO user = userService.getById(userId);

        HttpHeaders headers = new HttpHeaders();
        headers.add("X-User-ID", String.valueOf(user.getUserId()));

        return ResponseEntity.ok()
                .headers(headers)
                .body(user);
    }

    // Endpoint do aktualizacji użytkownika
    @PutMapping("/{userId}")
    public ResponseEntity<UserDTO> update(@PathVariable Long userId, @Valid @RequestBody UserDTO userDTO) {
        UserDTO updatedUser = userService.update(userId, userDTO);

        HttpHeaders headers = new HttpHeaders();
        headers.add("X-User-ID", String.valueOf(updatedUser.getUserId()));
        headers.add("X-Update-Timestamp", String.valueOf(System.currentTimeMillis()));

        return ResponseEntity.status(HttpStatus.OK)
                .headers(headers)
                .body(updatedUser);
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> delete(@PathVariable Long userId) {
        userService.delete(userId);

        HttpHeaders headers = new HttpHeaders();
        headers.add("X-User-ID", String.valueOf(userId));

        return ResponseEntity.noContent()
                .headers(headers)
                .build();
    }
}
