package ru.crm.system.service;

import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.crm.system.database.entity.Lesson;
import ru.crm.system.database.entity.enums.ActionType;
import ru.crm.system.database.repository.LessonRepository;
import ru.crm.system.dto.lesson.LessonCreateEditDto;
import ru.crm.system.dto.lesson.LessonReadDto;
import ru.crm.system.dto.loginfo.LogInfoCreateDto;
import ru.crm.system.listener.entity.AccessType;
import ru.crm.system.listener.entity.EntityEvent;
import ru.crm.system.mapper.lesson.LessonCreateEditMapper;
import ru.crm.system.mapper.lesson.LessonReadMapper;

import java.util.Optional;

import static java.time.LocalDateTime.now;
import static java.time.temporal.ChronoUnit.MINUTES;
import static java.time.temporal.ChronoUnit.SECONDS;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class LessonService {

    private final LessonRepository lessonRepository;
    private final LessonCreateEditMapper lessonCreateEditMapper;
    private final LessonReadMapper lessonReadMapper;
    private final ApplicationEventPublisher publisher;

    @Transactional
    public LessonReadDto create(LessonCreateEditDto createDto, Integer adminId) {
        return Optional.of(createDto)
                .map(lessonCreateEditMapper::map)
                .map(lessonRepository::save)
                .map(lesson -> {
                    var logInfo = createLessonLogInfo(adminId, lesson);
                    publisher.publishEvent(new EntityEvent<>(lesson, AccessType.CREATE, logInfo));
                    return lesson;
                })
                .map(lessonReadMapper::map)
                .orElseThrow();
    }

    /**
     * Метод для создания LogInfo при сохранении нового урока в базу данных
     * со статусом ATTENDING_A_LESSON из {@link ActionType} .
     */
    private LogInfoCreateDto createLessonLogInfo(Integer adminId, Lesson lesson) {
        return LogInfoCreateDto.builder()
                .action(ActionType.ATTENDING_A_LESSON)
                .description(String
                        .format("%s назначен новый урок ученику: %s %s. Учитель: %s %s. Предмет: %s",
                                lesson.getDateTime().truncatedTo(MINUTES),
                                lesson.getStudent().getUserInfo().getFirstName(),
                                lesson.getStudent().getUserInfo().getLastName(),
                                lesson.getTeacher().getUserInfo().getFirstName(),
                                lesson.getTeacher().getUserInfo().getLastName(),
                                lesson.getSubject().getName()))
                .createdAt(now().truncatedTo(SECONDS))
                .adminId(adminId)
                .studentId(lesson.getStudent().getId())
                .build();
    }
}