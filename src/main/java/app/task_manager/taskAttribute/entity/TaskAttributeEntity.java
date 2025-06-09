package app.task_manager.taskAttribute.entity;

import app.task_manager.taskAttribute.enums.TaskAttributeCategory;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "task_attributes", uniqueConstraints = @UniqueConstraint(columnNames = {"category", "name"}))
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TaskAttributeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private TaskAttributeCategory category;

    @Column(nullable = false, length = 50)
    private String name;
}
