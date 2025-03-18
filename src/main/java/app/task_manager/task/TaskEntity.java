package app.task_manager.task;


import app.task_manager.User.UserEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "tasks")
public class TaskEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(length = 4000)
    private String description;

    private LocalDateTime dueDate;

    // Relacja do statusu
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "status_id", referencedColumnName = "id")
    private TaskAttribute status;

    // Relacja do priorytetu
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "priority_id", referencedColumnName = "id")
    private TaskAttribute priority;

    private LocalDateTime completionDate;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    // Relacja do użytkowników przypisanych do zadania
    @ManyToMany
    @JoinTable(
            name = "task_assigned_users",
            joinColumns = @JoinColumn(name = "task_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private List<UserEntity> assignedUsers;

    // Relacja do podzadań (hierarchia)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_task_id")
    private TaskEntity parentTask;

    @OneToMany(mappedBy = "parentTask", cascade = CascadeType.ALL)
    private List<TaskEntity> subTasks;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
