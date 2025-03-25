package app.task_manager.User;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<app.task_manager.User.UserEntity, Long> {
    @Query("SELECT COUNT(*)>0 FROM UserEntity u WHERE u.email = :EMAIL")
    Boolean existByEmail(@Param("email") String email);

    Optional<UserEntity> findByEmail(String email);
}
