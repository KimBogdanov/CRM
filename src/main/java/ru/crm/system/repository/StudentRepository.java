package ru.crm.system.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.crm.system.model.Student;

import java.util.List;
import java.util.Optional;

public interface StudentRepository extends JpaRepository<Student, Integer> {

    Optional<Student> findByName(String name);
    List<Student> findAllByName(String partOfName);
    List<Student> findAllByNameContaining(String studentName);
    void deleteById(int id);
}
