package ru.crm.system.database.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import ru.crm.system.database.entity.enums.ObjectType;
import ru.crm.system.database.entity.enums.TaskType;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@EqualsAndHashCode(of = {"description", "createdDateTime"}, callSuper = true)
@ToString(exclude = {"id"}, callSuper = true)
@Table(name = "task")
@Entity
public class Task extends AbstractEntity {

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