package ru.crm.system.database.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import ru.crm.system.converter.MoneyConverter;
import ru.crm.system.database.entity.enums.AbonementStatus;
import ru.crm.system.database.entity.enums.AbonementType;

import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(exclude = {"id", "type"})
@ToString(exclude = {"id", "type"})
@Entity
public class Abonement implements BaseEntity<Integer> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Integer numberOfLessons;

    @Convert(converter = MoneyConverter.class)
    private BigDecimal balance;

    @Enumerated(EnumType.STRING)
    private AbonementType type;

    private LocalDate begin;

    private LocalDate expire;

    @Enumerated(EnumType.STRING)
    private AbonementStatus status;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Student student;
}