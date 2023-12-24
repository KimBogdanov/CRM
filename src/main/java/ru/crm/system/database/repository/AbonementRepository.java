package ru.crm.system.database.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.crm.system.database.entity.Abonement;

import java.math.BigDecimal;

public interface AbonementRepository extends JpaRepository<Abonement, Integer> {

    @Query("""
            select a.balance
            from Abonement a
            where a.status in ('ACTIVE', 'FROZEN')
            and a.student.id = :studentId
            """)
    BigDecimal getBalanceByStudent(Integer studentId);
}