package ru.crm.system.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.crm.system.database.entity.Subject;

import java.util.List;
import java.util.Optional;

public interface SubjectRepository extends JpaRepository<Subject, Integer> {
    /**
     * Выдает список предметов по части названия
     * @param name часть названия
     * @return список предметов
     */
    List<Subject> findAllByNameContaining(String name);

    /**
     * Проверяет есть ли такой предмет в БД
     * @param subjectName название предмета
     * @return boolean есть ли предмет
     */
    boolean existsByNameLikeIgnoreCase(String subjectName);

    /**
     * Возвращает предмет по Id
     * @param SubjectId Id предмета
     * @return предмет
     */
    Optional<Subject> findSubjectById(Integer SubjectId);

    /**
     * Возвращает предмет по имени
     * @param SubjectName название предмета
     * @return предмет
     */
    Optional<Subject> findSubjectByName(String SubjectName);
}
