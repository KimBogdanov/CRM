package ru.crm.system.database.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(of = "userInfo")
@ToString(exclude = {"subject", "abonements", "lessons"})
@Entity
public class Student implements BaseEntity<Integer> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private UserInfo userInfo;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Subject subject;

    @Builder.Default
    @OneToMany(mappedBy = "student")
    private List<Abonement> abonements = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "student")
    private List<Lesson> lessons = new ArrayList<>();
}