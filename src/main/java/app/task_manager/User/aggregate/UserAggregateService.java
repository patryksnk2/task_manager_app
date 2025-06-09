package app.task_manager.User.aggregate;

import app.task_manager.User.entity.UserEntity;
import app.task_manager.User.exception.UserNotFoundException;
import app.task_manager.User.repository.UserRepository;
import app.task_manager.roles.entity.RoleEntity;
import app.task_manager.roles.exception.RoleNotFoundException;
import app.task_manager.roles.repository.RoleRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserAggregateService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    public UserAggregate getCurrentUserAggregate() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        log.debug("Fetching current user aggregate for username='{}'", username);

        UserEntity user = userRepository.findByUsername(username).orElseThrow(() -> {
            log.warn("User '{}' not found in database", username);
            return new UserNotFoundException("User not found");
        });

        return UserAggregate.builder()
                .userEntity(user)
                .build();
    }

    public UserAggregate getAggregateByUserId(Long userId) {
        log.debug("Fetching user aggregate for userId={}", userId);

        UserEntity user = userRepository.findById(userId).orElseThrow(() -> {
            log.warn("User with id={} not found", userId);
            return new UserNotFoundException("User not found");
        });

        return UserAggregate.builder()
                .userEntity(user)
                .build();
    }

    @Transactional
    public void assignRoleToUser(Long targetUserId, Long roleId) {
        log.info("Assigning roleId={} to userId={}", roleId, targetUserId);

        UserAggregate currentUser = getCurrentUserAggregate();
        log.debug("Current user performing assignment: {}", currentUser.getUserEntity().getUsername());

        UserAggregate targetUser = getAggregateByUserId(targetUserId);

        RoleEntity role = roleRepository.findById(roleId).orElseThrow(() -> {
            log.warn("Role with id={} not found", roleId);
            return new RoleNotFoundException("Cannot find role with id: " + roleId);
        });

        targetUser.assignRole(role, currentUser);

        userRepository.save(targetUser.getUserEntity());

        log.info("Successfully assigned roleId={} to userId={}", roleId, targetUserId);
    }
}
