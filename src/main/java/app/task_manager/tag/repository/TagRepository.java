package app.task_manager.tag.repository;

import app.task_manager.tag.entity.TagEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;
@RepositoryRestResource(exported = false)
public interface TagRepository extends JpaRepository<TagEntity, Long> {
    @Query("select t from TagEntity t join t.tasks ts where ts.id = :id ")
    List<TagEntity> findAllByTaskId(@Param("id") Long id);

}
