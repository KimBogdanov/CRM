package ru.crm.system.database.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.crm.system.database.entity.Order;

public interface OrderRepository extends JpaRepository<Order, Integer> {

}