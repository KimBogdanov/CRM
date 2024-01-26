package ru.crm.system.database.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import ru.crm.system.converter.MoneyConverter;
import ru.crm.system.database.entity.enums.AbonementStatus;
import ru.crm.system.database.entity.enums.AbonementType;

import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.OneToOne;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@EqualsAndHashCode(exclude = {"id", "type"}, callSuper = true)
@ToString(exclude = {"id", "type"}, callSuper = true)
@Entity
public class Abonement extends AbstractEntity {

    private Integer numberOfLessons;

    @Convert(converter = MoneyConverter.class)
    private BigDecimal balance;

    @Enumerated(EnumType.STRING)
    private AbonementType type;

    private LocalDate begin;

    private LocalDate expire;

    @Enumerated(EnumType.STRING)
    private AbonementStatus status;

    @OneToOne(optional = false, fetch = FetchType.LAZY)
    private Student student;
}