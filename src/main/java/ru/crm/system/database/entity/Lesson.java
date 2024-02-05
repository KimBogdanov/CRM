package ru.crm.system.database.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import ru.crm.system.converter.MoneyConverter;
import ru.crm.system.database.entity.enums.LessonPayType;
import ru.crm.system.database.entity.enums.LessonStatus;
import ru.crm.system.database.entity.enums.LessonType;

import javax.persistence.CollectionTable;
import javax.persistence.Convert;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.CascadeType.MERGE;
import static javax.persistence.CascadeType.PERSIST;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@EqualsAndHashCode(exclude = {"students", "teacher"}, callSuper = true)
@ToString(exclude = {"students", "teacher", "subject", "comments", "logInfos"}, callSuper = true)
@Entity
public class Lesson extends AbstractEntity {

    @ManyToMany(cascade = {PERSIST, MERGE})
    @JoinTable(name = "student_lesson",
            joinColumns = @JoinColumn(name = "lesson_id"),
            inverseJoinColumns = @JoinColumn(name = "student_id"))
    private List<Student> students;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Teacher teacher;

    private LocalDate date;

    private LocalTime time;

    private Integer duration;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Subject subject;

    @Enumerated(EnumType.STRING)
    private LessonStatus status;

    @Enumerated(EnumType.STRING)
    private LessonPayType payType;

    @Enumerated(EnumType.STRING)
    private LessonType lessonType;

    private String description;

    @Convert(converter = MoneyConverter.class)
    private BigDecimal cost;

    @Builder.Default
    @ElementCollection
    @CollectionTable(name = "comment")
    private List<Comment> comments = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "lesson")
    private List<LogInfo> logInfos = new ArrayList<>();

    public void addComment(Comment comment) {
        comments.add(comment);
    }
}