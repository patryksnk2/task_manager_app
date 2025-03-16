package app.task_manager.task;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "task_attributes", uniqueConstraints = @UniqueConstraint(columnNames = {"category", "name"}))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TaskAttribute {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false, length = 20)
    private String category;

    @Column(nullable = false, length = 50)
    private String name;
}
