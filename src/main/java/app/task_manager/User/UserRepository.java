package app.task_manager.User;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<app.task_manager.User.UserEntity, Long> {
}
