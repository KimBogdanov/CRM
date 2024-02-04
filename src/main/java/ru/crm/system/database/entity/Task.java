package ru.crm.system.database.entity;

import lombok.*;
import ru.crm.system.database.entity.enums.TaskStatus;
import ru.crm.system.database.entity.enums.ObjectNestedTask;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(of = "description")
@ToString(exclude = {"id"})
@Entity
public class Task implements BaseEntity<Integer> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "description", nullable = false)
    private String description;
    @Enumerated(EnumType.STRING)
    @Column(name = "task_status")
    private TaskStatus taskStatus;
    @Enumerated(EnumType.STRING)
    @Column(name = "object_nested_task")
    private ObjectNestedTask objectNestedTask;
    @Column(name = "end_date_time", nullable = false)
    private LocalDateTime endDateTime;
    @Column(name = "nested_object_id")
    private Integer nestedObjectId;
}