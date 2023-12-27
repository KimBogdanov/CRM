package ru.crm.system.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.crm.system.database.entity.Subject;
import ru.crm.system.database.repository.SubjectRepository;
import ru.crm.system.dto.subject.SubjectCreateEditDto;
import ru.crm.system.dto.subject.SubjectReadDto;
import ru.crm.system.mapper.subject.SubjectCreateEditMapper;
import ru.crm.system.mapper.subject.SubjectReadMapper;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SubjectService {

    private final SubjectRepository subjectRepository;
    private final SubjectCreateEditMapper subjectCreateEditMapper;
    private final SubjectReadMapper subjectReadMapper;

    /**
     * Возвращает список всех предметов
     *
     * @return список всех предметов
     */
    public List<SubjectReadDto> getAll() {
        return subjectRepository.findAll().stream()
                .map(subjectReadMapper::map)
                .toList();
    }

    /**
     * Возвращает список всех предметов по части названия
     * например для выпадающего списка предметов
     *
     * @param name часть названия предмета
     * @return список всех предметов
     */
    public List<Subject> findAllByNameContaining(String name) {
        return subjectRepository.findAllByNameContaining(name);
    }

    /**
     * возвращает предмет по id
     *
     * @param id - id предмета
     * @return предмет
     */
    public SubjectReadDto getById(Integer id) {
        return subjectRepository.findById(id)
                .map(subjectReadMapper::map)
                .orElseThrow(() -> new EntityNotFoundException("Предмет с id " + id + " не найден в базе данных"));
    }

//    /**
//     * сохраняет предмет в БД или выдает ошибку, если он там уже есть
//     *
//     * @param subject предмет
//     * @return сохраненный предмет, если сохранение удачно
//     */
//    public Subject save(Subject subject) {
//
//        if (subjectRepository.existsByNameLikeIgnoreCase(subject.getName())) {
//            throw new EntityExistsException("Предмет " + subject.getName() + " уже есть в базе данных");
//        }
//        return subjectRepository.save(subject);
//    }

    public SubjectReadDto save(SubjectCreateEditDto createDto) {
        return Optional.of(createDto)
                .map(subjectCreateEditMapper::map)
                .map(subjectRepository::save)
                .map(subjectReadMapper::map)
                .orElseThrow();
    }

    /**
     * Удаляет предмет из БД по id
     *
     * @param id id предмета
     */
    public void delete(int id) {
        subjectRepository.deleteById(id);
    }
}