package ru.crm.system.database.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import ru.crm.system.converter.MoneyConverter;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@EqualsAndHashCode(of = "userInfo", callSuper = true)
@ToString(exclude = {"id", "orders", "logInfos", "salaryLogs"}, callSuper = true)
@Entity
public class Admin extends AbstractEntity {

    private UserInfo userInfo;

    @Column(nullable = false)
    @Convert(converter = MoneyConverter.class)
    private BigDecimal shiftRate;

    @Builder.Default
    @OneToMany(mappedBy = "admin")
    private List<Order> orders = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "admin")
    private List<LogInfo> logInfos = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "admin")
    private List<SalaryLog> salaryLogs = new ArrayList<>();
}