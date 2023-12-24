package ru.crm.system.database.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.crm.system.database.entity.LogInfo;

public interface LogInfoRepository extends JpaRepository<LogInfo, Integer> {
}