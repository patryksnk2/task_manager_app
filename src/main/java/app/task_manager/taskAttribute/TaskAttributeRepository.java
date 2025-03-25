package app.task_manager.taskAttribute;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskAttributeRepository extends JpaRepository<TaskAttributeEntity, Long> {
}
