package ru.crm.system.database.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import ru.crm.system.database.entity.enums.AbonementStatus;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(exclude = {"id", "type"})
@ToString(exclude = {"id", "type"})
@Entity
public class Abonement implements BaseEntity<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer numberOfLessons;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private AbonementType type;

    private LocalDate begin;

    private LocalDate expire;

    @Enumerated(EnumType.STRING)
    private AbonementStatus status;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Student student;
}