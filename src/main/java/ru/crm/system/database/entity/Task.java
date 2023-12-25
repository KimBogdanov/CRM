package ru.crm.system.database.entity;

import lombok.*;
import ru.crm.system.database.entity.enums.ObjectType;
import ru.crm.system.database.entity.enums.TaskType;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(of = {"description", "createdDateTime"})
@ToString(exclude = {"id"})
@Table(name = "task")
@Entity
public class Task implements BaseEntity<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private ObjectType objectType;

    @Column
    private String description;

    @Column(nullable = false)
    private LocalDateTime createdDateTime;

    @Column(nullable = false)
    private LocalDateTime endDateTime;

    @Enumerated(EnumType.STRING)
    private TaskType taskType;
}