package ru.crm.system.service;

import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import ru.crm.system.database.entity.Lesson;
import ru.crm.system.database.entity.enums.LessonStatus;
import ru.crm.system.database.repository.AbonementRepository;
import ru.crm.system.database.repository.LessonRepository;
import ru.crm.system.dto.lesson.LessonCreateEditDto;
import ru.crm.system.dto.lesson.LessonReadDto;
import ru.crm.system.dto.loginfo.LogInfoCreateDto;
import ru.crm.system.listener.entity.AccessType;
import ru.crm.system.listener.entity.EntityEvent;
import ru.crm.system.mapper.lesson.LessonCreateEditMapper;
import ru.crm.system.mapper.lesson.LessonReadMapper;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validator;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Validated(value = LessonCreateEditDto.class)
public class LessonService {

    private final LessonRepository lessonRepository;
    private final LessonCreateEditMapper lessonCreateEditMapper;
    private final LessonReadMapper lessonReadMapper;
    private final ApplicationEventPublisher publisher;
    private final AbonementRepository abonementRepository;
    private final AbonementService abonementService;
    private final LogInfoService logInfoService;
    private final SalaryService salaryService;
    private final CommentService commentService;
    private final Validator validator;

    @Transactional
    public Optional<LessonReadDto> create(LessonCreateEditDto createDto) {
        validateLessonDto(createDto);

        return Optional.of(createDto)
                .map(lessonCreateEditMapper::map)
                .map(lessonRepository::save)
                .map(lesson -> {
                    var logInfo = logInfoService.createLogInfoWhenLessonAppointed(lesson);
                    publisher.publishEvent(new EntityEvent<>(lesson, AccessType.CREATE, logInfo));
                    return lesson;
                })
                .map(lessonReadMapper::map);
    }

    @Transactional
    public Optional<LessonReadDto> update(Integer lessonId, LessonCreateEditDto editDto) {
        validateLessonDto(editDto);

        return lessonRepository.findById(lessonId)
                .map(lesson -> lessonCreateEditMapper.map(editDto, lesson))
                .map(lessonRepository::saveAndFlush)
                .map(lessonReadMapper::map);
    }

    private void validateLessonDto(LessonCreateEditDto lessonDto) {
        var violations = validator.validate(lessonDto);
        if (!violations.isEmpty()) {
            var violationMessages = new StringBuilder();
            for (ConstraintViolation<LessonCreateEditDto> violation : violations) {
                violationMessages.append(violation.getMessage());
            }
            throw new ConstraintViolationException(violationMessages.toString(), violations);
        }
    }

    @Transactional
    public Optional<LessonReadDto> changeLessonStatus(Integer lessonId, LessonStatus status) {
        return lessonRepository.findById(lessonId)
                .map(lesson -> {
                    lesson.setStatus(status);
                    lessonRepository.flush();
                    switch (status) {
                        case SUCCESSFULLY_FINISHED -> jobInCaseLessonFinishedSuccessfully(lesson);
                        case CANCELED_FOR_GOOD_REASON -> jobInCaseLessonCanceledForGoodReason(lesson);
                        case CANCELED_BY_TEACHER_FAULT -> jobInCaseLessonCanceledByTeacherFault(lesson);
                        case CANCELED -> jobInCaseLessonCanceled(lesson);
                    }
                    return lesson;
                })
                .map(lessonReadMapper::map);
    }

    @Transactional
    public Optional<LessonReadDto> addComment(Integer lessonId, String text) {
        return lessonRepository.findById(lessonId)
                .map(lesson -> {
                    var comment = commentService.createComment(text);
                    lesson.addComment(comment);
                    var logInfo = logInfoService.createCommentLogInfo(lesson, text);
                    publisher.publishEvent(new EntityEvent<>(lesson, AccessType.UPDATE, logInfo));
                    return lesson;
                })
                .map(lessonReadMapper::map);
    }

    public Optional<LessonReadDto> findById(Integer id) {
        return lessonRepository.findById(id)
                .map(lessonReadMapper::map);
    }

    /**
     * The method is executed in case of a successful lesson,
     * i.e. the teacher changed the lesson status to SUCCESSFULLY_FINISHED:<br>
     * - The cost of the lesson is debited from the student’s subscription balance;<br>
     * - The lesson is written off from the subscription;<br>
     * - The teacher is paid for the lesson taught;<br>
     * - Records are made in the log_info table about the operations performed;
     */
    private void jobInCaseLessonFinishedSuccessfully(Lesson lesson) {
        abonementService.writeOffMoneyFromStudentBalance(lesson);
        abonementService.subtractOneLessonFromAbonement(lesson);
        salaryService.addMoneyIntoTeacherAccount(lesson);
        var teacherLogInfo = logInfoService.createLogInfoWhenTeacherGetsPayment(lesson);
        publisher.publishEvent(new EntityEvent<>(lesson, AccessType.UPDATE, teacherLogInfo));
        var studentLogInfos = logInfoService.createLogInfosWhenWriteOffFromStudentsBalance(lesson);
        publishStudentEvent(lesson, studentLogInfos);
        abonementRepository.flush();
    }

    private void publishStudentEvent(Lesson lesson, List<LogInfoCreateDto> logInfos) {
        for (LogInfoCreateDto logInfo : logInfos) {
            publisher.publishEvent(new EntityEvent<>(lesson, AccessType.UPDATE, logInfo));
        }
    }

    /**
     * The method is performed in case of cancellation of a lesson for a valid reason, i.e.
     * the teacher changed the lesson status to CANCELED_FOR_GOOD_REASON:<br>
     * - The teacher is paid for the lesson taught; <br>
     * - Entries are made in the log_info table about the transfer
     * of money to the teacher’s balance and about the cancellation of the lesson;
     */
    private void jobInCaseLessonCanceledForGoodReason(Lesson lesson) {
        salaryService.addMoneyIntoTeacherAccount(lesson);
        var teacherLogInfo = logInfoService.createLogInfoWhenTeacherGetsPayment(lesson);
        publisher.publishEvent(new EntityEvent<>(lesson, AccessType.UPDATE, teacherLogInfo));
        var goodReasonLogInfos = logInfoService.createLogInfoWhenLessonCanceledByGoodReason(lesson);
        goodReasonLogInfos.forEach(logInfo -> publisher.publishEvent(new EntityEvent<>(lesson, AccessType.UPDATE, logInfo)));
    }

    /**
     * The method is performed in the event of a lesson being canceled due to the teacher, i.e.
     * the teacher changed the lesson status to CANCELED_BY_TEACHER_FAULT <br>
     * - An entry is made in the log_info table about the lesson being canceled due to the teacher's fault.;
     */
    private void jobInCaseLessonCanceledByTeacherFault(Lesson lesson) {
        var teacherReasonLogInfo = logInfoService.createLogInfoWhenLessonCanceledByTeacherReason(lesson);
        publisher.publishEvent(new EntityEvent<>(lesson, AccessType.UPDATE, teacherReasonLogInfo));
        // TODO: 12/24/2023 add email sending by publisher
    }

    /**
     * The method is performed in the event of a lesson being canceled due to the teacher, i.e.
     * the teacher changed the lesson status to CANCELED <br>
     * - An entry is made in the log_info table about the cancellation of the lesson due to the teacher.;
     */
    private void jobInCaseLessonCanceled(Lesson lesson) {
        var canceledLessonLogInfo = logInfoService.createLogInfoWhenLessonCanceled(lesson);
        publisher.publishEvent(new EntityEvent<>(lesson, AccessType.UPDATE, canceledLessonLogInfo));
        // TODO: 12/24/2023 add email sending and phone by publisher
    }
}