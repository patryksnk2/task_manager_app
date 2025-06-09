package app.task_manager.task.repository;

import app.task_manager.task.entity.TaskEntity;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RepositoryRestResource(exported = false)
public interface TaskRepository extends JpaRepository<TaskEntity, Long> {

    @EntityGraph(attributePaths = {"assignedUsers"})
    @Query("SELECT t FROM TaskEntity t")
    List<TaskEntity> findAllWithAssignedUsers();


}
