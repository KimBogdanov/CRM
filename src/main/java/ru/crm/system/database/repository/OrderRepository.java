package ru.crm.system.database.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import ru.crm.system.database.entity.Order;
import ru.crm.system.database.entity.enums.OrderStatus;

public interface OrderRepository extends JpaRepository<Order, Integer> {

    @Modifying
    @Query("""
            update Order o
            set o.status = :status
            """)
    void changeStatus(OrderStatus status);
}