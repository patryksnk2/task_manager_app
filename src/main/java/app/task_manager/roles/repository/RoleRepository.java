package app.task_manager.roles.repository;

import app.task_manager.User.entity.UserEntity;
import app.task_manager.roles.entity.RoleEntity;
import app.task_manager.roles.enums.RoleName;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;
import java.util.Optional;

@RepositoryRestResource(exported = false)
public interface RoleRepository extends JpaRepository<RoleEntity, Long> {
    List<RoleEntity> findByUsersContains(UserEntity user);

    Optional<RoleEntity> findByRole(RoleName role);
}
