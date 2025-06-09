package app.task_manager.User.repository;

import app.task_manager.User.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RepositoryRestResource(exported = false)
public interface UserRepository extends JpaRepository<UserEntity, Long> {
    @Query("SELECT COUNT(*)>0 FROM UserEntity u WHERE u.email = :email")
    Boolean existByEmail(@Param("email") String email);

    Optional<UserEntity> findByEmail(String email);

    @Query("SELECT u FROM UserEntity u LEFT JOIN FETCH u.roles WHERE u.username = :username")
    Optional<UserEntity> findByUsername(@Param("username") String username);

    List<UserEntity> findByRolesRole(String role);
}
