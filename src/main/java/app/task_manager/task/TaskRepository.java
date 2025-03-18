package app.task_manager.task;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TaskRepository extends JpaRepository<TaskEntity, Long> {
      //Optional<TaskAttribute> findTaskAttributesById(Long taskAttributesId);

//    @Query("SELECT t FROM Task t WHERE t.dueDate <= :dueDate AND t.task_attributes_id = (SELECT ID from TASK_ATTRIBUTES WHERE  )")
//    List<Task> findDueTasksByStatus(@Param("dueDate") LocalDateTime dueDate, @Param("status") String status);


}
