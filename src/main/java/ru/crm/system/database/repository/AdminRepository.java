package ru.crm.system.database.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.crm.system.database.entity.Admin;

public interface AdminRepository extends JpaRepository<Admin, Integer> {

}