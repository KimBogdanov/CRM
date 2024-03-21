package ru.crm.system.database.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import ru.crm.system.database.entity.enums.TeacherStatus;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@EqualsAndHashCode(of = "userInfo", callSuper = true)
@ToString(exclude = {"lessons", "subjects", "logInfos", "salaryLogs"}, callSuper = true)
@Entity
public class Teacher extends AbstractEntity {

    private UserInfo userInfo;

    @Builder.Default
    @ManyToMany
    @JoinTable(
            name = "teacher_subject",
            joinColumns = @JoinColumn(name = "teacher_id"),
            inverseJoinColumns = @JoinColumn(name = "subject_id"))
    private List<Subject> subjects = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    private TeacherStatus status;

    @Builder.Default
    @OneToMany(mappedBy = "teacher")
    private List<Lesson> lessons = new ArrayList<>();

    BigDecimal salaryPerHour;

    @Builder.Default
    @OneToMany(mappedBy = "teacher")
    private List<LogInfo> logInfos = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "teacher")
    private List<SalaryLog> salaryLogs = new ArrayList<>();

    private Double payRatio;

    public void addSalaryLog(SalaryLog salaryLog) {
        salaryLogs.add(salaryLog);
        salaryLog.setTeacher(this);
    }
}