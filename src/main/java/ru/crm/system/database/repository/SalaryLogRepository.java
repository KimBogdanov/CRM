package ru.crm.system.database.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.crm.system.database.entity.SalaryLog;

public interface SalaryLogRepository extends JpaRepository<SalaryLog, Integer> {

}