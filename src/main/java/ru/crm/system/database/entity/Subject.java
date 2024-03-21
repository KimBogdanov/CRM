package ru.crm.system.database.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@EqualsAndHashCode(exclude = {"id", "students", "teachers"}, callSuper = true)
@ToString(exclude = {"id", "students", "teachers"}, callSuper = true)
@Entity
public class Subject extends AbstractEntity {

    private String name;

    @Builder.Default
    @OneToMany(mappedBy = "subject")
    private List<Student> students = new ArrayList<>();

    @Builder.Default
    @ManyToMany(mappedBy = "subjects")
    private List<Teacher> teachers = new ArrayList<>();
}