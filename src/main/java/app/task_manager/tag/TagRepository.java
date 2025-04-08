package app.task_manager.tag;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TagRepository extends JpaRepository<TagEntity, Long> {
    @Query("select t from TagEntity t join t.tasks ts where ts.id = :taskId ")
    List<TagEntity> findALlByTaskId(@Param("taskId") Long taskId);
}
