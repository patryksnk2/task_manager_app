package app.task_manager.taskAttribute;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
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

    @Column(nullable = false, length = 20)
    private String category;

    @Column(nullable = false, length = 50)
    private String name;
}
