package ru.crm.system.database.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import ru.crm.system.database.entity.enums.LessonStatus;
import ru.crm.system.database.entity.enums.LessonType;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(exclude = {"id", "student", "teacher"})
@ToString(exclude = {"student", "teacher"})
@Entity
public class Lesson implements BaseEntity<Integer> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Student student;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Teacher teacher;

    private LocalDateTime dateTime;

    private Integer duration;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Subject subject;

    @Enumerated(EnumType.STRING)
    private LessonStatus status;

    @Enumerated(EnumType.STRING)
    private LessonType type;

    private String description;

    private BigDecimal cost;
}