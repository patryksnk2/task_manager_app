package app.task_manager.User.controller;

import app.task_manager.User.dto.UserDTO;
import app.task_manager.User.service.UserService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/users")
@Slf4j
@AllArgsConstructor
public class UserController {
    private final UserService userService;

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<List<UserDTO>> getAll() {
        log.info("Received request to fetch all users");
        List<UserDTO> users = userService.getAll();

        HttpHeaders headers = new HttpHeaders();
        headers.add("X-Total-Users", String.valueOf(users.size()));

        return ResponseEntity.ok().headers(headers).body(users);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{userId}")
    public ResponseEntity<UserDTO> getById(@PathVariable Long userId) {
        log.info("Fetching user with ID={}", userId);
        UserDTO user = userService.getById(userId);

        HttpHeaders headers = new HttpHeaders();
        headers.add("X-User-ID", String.valueOf(user.getUserId()));
        log.debug("User fetched: {}", user.getUsername());
        return ResponseEntity.ok().headers(headers).body(user);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{userId}")
    public ResponseEntity<String> update(@PathVariable Long userId, @Valid @RequestBody UserDTO userDTO) {
        log.info("Updating user with ID={}", userId);

        UserDTO updatedUser = userService.update(userId, userDTO);

        HttpHeaders headers = new HttpHeaders();
        headers.add("X-User-ID", String.valueOf(updatedUser.getUserId()));
        headers.add("X-Update-Timestamp", String.valueOf(System.currentTimeMillis()));

        URI location = ServletUriComponentsBuilder.fromCurrentRequest().build().toUri();
        headers.setLocation(location);

        log.info("User with ID={} updated successfully", userId);
        return ResponseEntity.status(HttpStatus.OK).headers(headers).body("User updated successfully!");
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> delete(@PathVariable Long userId) {
        log.info("Deleting user with ID={}", userId);
        userService.delete(userId);

        HttpHeaders headers = new HttpHeaders();
        headers.add("X-User-ID", String.valueOf(userId));
        headers.add("X-Deleted-By", getCurrentUser().getUsername());
        log.info("User with ID={} deleted", userId);
        return ResponseEntity.noContent().headers(headers).build();
    }

    @GetMapping("/current-user")
    public UserDTO getCurrentUser() {
        return userService.getCurrentUser();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/{userId}/roles/{roleId}")
    @Transactional
    public ResponseEntity<?> assignRoleToUser(@PathVariable Long userId, @PathVariable Long roleId) {
        log.info("Assigning roleId={} to userId={}", roleId, userId);
        HttpHeaders headers = new HttpHeaders();
        headers.add("X-User-ID", String.valueOf(userId));
        headers.add("X-Assigned-Role-ID", String.valueOf(roleId));
        headers.add("X-Assigned-By", getCurrentUser().getUsername());
        try {
            userService.assignRoleToUser(userId, roleId);
            log.info("Successfully assigned roleId={} to userId={}", roleId, userId);
            return ResponseEntity.ok().headers(headers).body("Rola została przypisana użytkownikowi.");
        } catch (RuntimeException e) {
            log.error("Error assigning roleId={} to userId={}: {}", roleId, userId, e.getMessage());
            return ResponseEntity.badRequest().headers(headers).body(e.getMessage());
        }
    }

}
