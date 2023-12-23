package ru.crm.system.database.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.crm.system.database.entity.AbonementType;

public interface AbonementTypeRepository extends JpaRepository<AbonementType, Integer> {

    @Query("""
            select at
            from AbonementType at
            where at.name = :name
            """)
    AbonementType getTypeByName(String name);
}