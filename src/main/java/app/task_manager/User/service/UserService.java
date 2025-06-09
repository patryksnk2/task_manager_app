package app.task_manager.User.service;

import app.task_manager.User.aggregate.UserAggregateService;
import app.task_manager.User.dto.UserDTO;
import app.task_manager.User.dto.UserRegistrationDTO;
import app.task_manager.User.entity.UserEntity;
import app.task_manager.User.exception.EmailAlreadyExistsException;
import app.task_manager.User.exception.UserNotFoundException;
import app.task_manager.User.mapper.UserMapper;
import app.task_manager.User.repository.UserRepository;
import app.task_manager.roles.entity.RoleEntity;
import app.task_manager.roles.enums.RoleName;
import app.task_manager.roles.exception.RoleNotFoundException;
import app.task_manager.roles.repository.RoleRepository;
import app.task_manager.task.entity.TaskEntity;
import app.task_manager.task.repository.TaskRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
@AllArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;
    private final UserAggregateService userAggregateService;
    private final TaskRepository taskRepository;


    @PreAuthorize("hasRole('ADMIN')")
    public List<UserDTO> getAll() {
        log.info("Fetching all users");
        List<UserEntity> users = userRepository.findAll();
        log.debug("Found {} users", users.size());
        return userMapper.toDTOList(users);
    }

    public UserDTO getById(Long userId) {
        log.info("Fetching user by ID={}", userId);

        UserEntity userEntity = userRepository.findById(userId).orElseThrow(() -> {
            log.warn("User with ID={} not found", userId);
            return new UserNotFoundException("User not found");
        });

        log.debug("User found: {}", userEntity.getUsername());
        return userMapper.toDTO(userEntity);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public UserDTO update(Long userId, UserDTO userDTO) {
        log.info("Updating user with ID={}", userId);

        UserEntity userEntity = userRepository.findById(userId).orElseThrow(() -> {
            log.warn("User with ID={} not found for update", userId);
            return new UserNotFoundException("User not found");
        });

        userMapper.updateEntityFromDto(userEntity, userDTO);

        if (userDTO.getRolesIds() != null) {
            List<RoleEntity> roles = roleRepository.findAllById(userDTO.getRolesIds());
            userEntity.setRoles(roles);
            log.debug("Updated roles for user ID={}", userId);
        }

        if (userDTO.getTaskIds() != null) {
            List<TaskEntity> tasks = taskRepository.findAllById(userDTO.getTaskIds());
            userEntity.setTasks(tasks);
            log.debug("Updated tasks for user ID={}", userId);
        }

        UserEntity updatedUser = userRepository.saveAndFlush(userEntity);
        log.info("User with ID={} updated successfully", userId);

        return userMapper.toDTO(updatedUser);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public void delete(Long userId) {
        log.info("Deleting user with ID={}", userId);

        if (!userRepository.existsById(userId)) {
            log.warn("User with ID={} not found for deletion", userId);
            throw new UserNotFoundException("User not found");
        }

        userRepository.deleteById(userId);
        log.info("User with ID={} deleted successfully", userId);
    }

    @Transactional
    public UserDTO getCurrentUser() {
        String username = getCurrentUsername();
        log.info("Fetching current user by username '{}'", username);

        UserEntity userEntity = userRepository.findByUsername(username).orElseThrow(() -> {
            log.error("Current user '{}' not found", username);
            return new UserNotFoundException("User not found");
        });

        log.debug("Current user '{}' fetched successfully", username);
        return userMapper.toDTO(userEntity);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public void assignRoleToUser(Long userId, Long roleId) {
        log.info("Assigning role ID={} to user ID={}", roleId, userId);
        userAggregateService.assignRoleToUser(userId, roleId);
        log.info("Role ID={} assigned to user ID={}", roleId, userId);
    }

    @Transactional
    public UserDTO register(UserRegistrationDTO dto) {
        log.info("Registering new user with email={}", dto.getEmail());

        if (userRepository.existByEmail(dto.getEmail())) {
            log.warn("Registration failed: email={} already exists", dto.getEmail());
            throw new EmailAlreadyExistsException("Email already exist");
        }

        UserEntity user = UserEntity.builder().username(dto.getUsername()).email(dto.getEmail()).password(passwordEncoder.encode(dto.getPassword())).build();

        RoleEntity userRole = roleRepository.findByRole(RoleName.USER).orElseThrow(() -> {
            log.error("Default USER role not found in database");
            return new RoleNotFoundException("There is no role named: " + RoleName.USER);
        });

        user.setRoles(List.of(userRole));
        UserEntity saved = userRepository.save(user);

        log.info("User '{}' registered successfully with ID={}", saved.getUsername(), saved.getUserId());
        return userMapper.toDTO(saved);
    }

    private String getCurrentUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getName();
    }
}
