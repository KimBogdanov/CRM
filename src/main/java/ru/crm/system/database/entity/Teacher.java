package ru.crm.system.database.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import ru.crm.system.database.entity.enums.TeacherStatus;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(of = "userInfo")
@ToString(exclude = {"lessons", "subjects"})
@Entity
public class Teacher implements BaseEntity<Integer> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private UserInfo userInfo;

    private BigDecimal salaryPerHour;

    @Builder.Default
    @ManyToMany
    @JoinTable(
            name = "teachers_subject",
            joinColumns = @JoinColumn(name = "teacher_id"),
            inverseJoinColumns = @JoinColumn(name = "subject_id"))
    private List<Subject> subjects = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    private TeacherStatus status;

    @Builder.Default
    @OneToMany(mappedBy = "teacher")
    private List<Lesson> lessons = new ArrayList<>();

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private TeacherSalary teacherSalary;

    public void addLesson(Lesson lesson) {
        lessons.add(lesson);
        lesson.setTeacher(this);
    }
}