package app.task_manager.tag.entity;

import app.task_manager.tag.enums.TagName;
import app.task_manager.task.entity.TaskEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "tags", uniqueConstraints = @UniqueConstraint(columnNames = {"name"}))
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TagEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long tag_id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    TagName name;

    @ManyToMany(mappedBy = "tags")
    private List<TaskEntity> tasks;

}
