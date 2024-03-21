package ru.crm.system.database.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.CascadeType.MERGE;
import static javax.persistence.CascadeType.PERSIST;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@EqualsAndHashCode(of = "userInfo", callSuper = true)
@ToString(exclude = {"subject", "lessons", "logInfos", "abonement"}, callSuper = true)
@Entity
public class Student  extends AbstractEntity {

    private UserInfo userInfo;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Subject subject;

    @OneToOne(mappedBy = "student")
    private Abonement abonement;

    @Builder.Default
    @ManyToMany(cascade = {PERSIST, MERGE})
    @JoinTable(name = "student_lesson",
            joinColumns = @JoinColumn(name = "student_id"),
            inverseJoinColumns = @JoinColumn(name = "lesson_id"))
    private List<Lesson> lessons = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "student")
    private List<LogInfo> logInfos = new ArrayList<>();
}