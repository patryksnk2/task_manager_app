package app.task_manager.User.aggregate;

import app.task_manager.User.entity.UserEntity;
import app.task_manager.roles.entity.RoleEntity;
import app.task_manager.roles.exception.RoleAlreadyExistException;
import app.task_manager.roles.exception.RoleNotFoundException;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Data
@NoArgsConstructor
@Builder
@Slf4j
@AllArgsConstructor
public class UserAggregate {

    private UserEntity userEntity;

    public List<RoleEntity> roles() {
        return List.copyOf(userEntity.getRoles());
    }

    public void assignRole(@Valid RoleEntity roleToAssign, UserAggregate currentUser) {
        Long targetUserId = userEntity.getUserId();
        Long roleId = roleToAssign.getRoleId();
        String currentUsername = currentUser.getUserEntity().getUsername();

        log.info("Attempting to assign roleId={} to userId={} by '{}'", roleId, targetUserId, currentUsername);

        boolean alreadyHasRole = userEntity.getRoles().stream()
                .anyMatch(role -> role.getRoleId().equals(roleId));
        log.debug("UserId={} alreadyHasRole={} for roleId={}", targetUserId, alreadyHasRole, roleId);

        if (alreadyHasRole) {
            log.warn("UserId={} already has roleId={}", targetUserId, roleId);
            throw new RoleAlreadyExistException("User already has this role.");
        }

        userEntity.getRoles().add(roleToAssign);

        log.info("RoleId={} successfully assigned to userId={}", roleId, targetUserId);
    }

}
