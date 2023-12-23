package ru.crm.system.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.crm.system.database.entity.Subject;
import ru.crm.system.database.repository.SubjectRepository;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SubjectService {

    private final SubjectRepository subjectRepository;

    /**
     * Возвращает список всех предметов
     * @return список всех предметов
     */
    public List<Subject> getAll() {
        return subjectRepository.findAll();
    }

    /**
     * Возвращает список всех предметов по части названия
     * например для выпадающего списка предметов
     * @param name часть названия предмета
     * @return список всех предметов
     */
    public List<Subject> findAllByNameContaining(String name) {
        return subjectRepository.findAllByNameContaining(name);
    }

    /**
     * возвращает предмет по id
     * @param id - id предмета
     * @return предмет
     */
    public Subject getById(Integer id) {
        return subjectRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Предмет с id " + id + " не найден в базе данных"));
    }

    /**
     * сохраняет предмет в БД или выдает ошибку, если он там уже есть
     * @param subject предмет
     * @return сохраненный предмет, если сохранение удачно
     */
    public Subject save(Subject subject) {

        if (subjectRepository.existsByNameLikeIgnoreCase(subject.getName())) {
            throw new EntityExistsException("Предмет " + subject.getName() + " уже есть в базе данных");
        }
        return subjectRepository.save(subject);
    }

    /**
     * Удаляет предмет из БД по id
     * @param id id предмета
     */
    public void delete(int id) {
        subjectRepository.deleteById(id);
    }
}