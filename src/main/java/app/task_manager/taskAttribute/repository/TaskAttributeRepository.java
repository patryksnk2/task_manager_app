package app.task_manager.taskAttribute.repository;

import app.task_manager.taskAttribute.entity.TaskAttributeEntity;
import app.task_manager.taskAttribute.enums.TaskAttributeCategory;
import app.task_manager.taskAttribute.enums.TaskPriority;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RepositoryRestResource(exported = false)
public interface TaskAttributeRepository extends JpaRepository<TaskAttributeEntity, Long> {

    Optional<TaskAttributeEntity> findByCategoryAndName(TaskAttributeCategory category, String name);
}
