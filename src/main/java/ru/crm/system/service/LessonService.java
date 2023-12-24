package ru.crm.system.service;

import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.crm.system.database.entity.Lesson;
import ru.crm.system.database.entity.SalaryLog;
import ru.crm.system.database.entity.enums.ActionType;
import ru.crm.system.database.entity.enums.LessonStatus;
import ru.crm.system.database.repository.AbonementRepository;
import ru.crm.system.database.repository.LessonRepository;
import ru.crm.system.database.repository.SalaryLogRepository;
import ru.crm.system.dto.lesson.LessonCreateEditDto;
import ru.crm.system.dto.lesson.LessonReadDto;
import ru.crm.system.dto.loginfo.LogInfoCreateDto;
import ru.crm.system.listener.entity.AccessType;
import ru.crm.system.listener.entity.EntityEvent;
import ru.crm.system.mapper.lesson.LessonCreateEditMapper;
import ru.crm.system.mapper.lesson.LessonReadMapper;

import javax.persistence.EntityNotFoundException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.Optional;

import static java.time.LocalDateTime.now;
import static java.time.temporal.ChronoUnit.MINUTES;
import static java.time.temporal.ChronoUnit.SECONDS;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class LessonService {

    private static final double MINUTES_IN_HOUR = 60.0;

    private final LessonRepository lessonRepository;
    private final LessonCreateEditMapper lessonCreateEditMapper;
    private final LessonReadMapper lessonReadMapper;
    private final ApplicationEventPublisher publisher;
    private final AbonementRepository abonementRepository;
    private final SalaryLogRepository salaryLogRepository;


    @Transactional
    public LessonReadDto create(LessonCreateEditDto createDto, Integer adminId) {
        return Optional.of(createDto)
                .map(lessonCreateEditMapper::map)
                .map(lessonRepository::save)
                .map(lesson -> {
                    var logInfo = createLogInfoWhenLessonAppointed(adminId, lesson);
                    publisher.publishEvent(new EntityEvent<>(lesson, AccessType.CREATE, logInfo));
                    return lesson;
                })
                .map(lessonReadMapper::map)
                .orElseThrow();
    }

    @Transactional
    public Optional<LessonReadDto> update(Integer lessonId,
                                          LessonCreateEditDto editDto) {
        return lessonRepository.findById(lessonId)
                .map(lesson -> lessonCreateEditMapper.map(editDto, lesson))
                .map(lessonRepository::saveAndFlush)
                .map(lessonReadMapper::map);
    }

    @Transactional
    public LessonReadDto changeLessonStatus(Integer lessonId,
                                            LessonStatus status) {
        return lessonRepository.findById(lessonId)
                .map(lesson -> {
                    lessonRepository.changeLessonStatus(status);
                    lessonRepository.flush();
                    switch (status) {
                        case SUCCESSFULLY_FINISHED -> jobInCaseLessonFinishedSuccessfully(lesson);
                        case CANCELED_FOR_GOOD_REASON -> jobInCaseLessonCanceledForGoodReason(lesson);
                        case CANCELED_BY_TEACHER_FAULT -> jobInCaseLessonCanceledByTeacherFault(lesson);
                        case CANCELED -> jobInCaseLessonCanceled(lesson);
                    }
                    return lesson;
                })
                .map(lessonReadMapper::map)
                .orElseThrow(() -> new EntityNotFoundException("Урок с номером " + lessonId + " не найден в базе данных"));
    }

    /**
     * Метод для создания LogInfo при сохранении нового урока в базу данных
     * со статусом ATTENDING_A_LESSON из {@link ActionType} .
     */
    private LogInfoCreateDto createLogInfoWhenLessonAppointed(Integer adminId, Lesson lesson) {
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

    /**
     * Метод для создания LogInfo при списании денег с баланса абонемента ученика
     */
    private LogInfoCreateDto createLogInfoWhenWriteOffFromStudentBalance(Lesson lesson) {
        return LogInfoCreateDto.builder()
                .action(ActionType.WRITE_OFF_FROM_STUDENT_BALANCE)
                .description(String
                        .format("С баланса ученика %s %s было списано %s руб.",
                                lesson.getStudent().getUserInfo().getFirstName(),
                                lesson.getStudent().getUserInfo().getLastName(),
                                lesson.getCost()))
                .createdAt(now().truncatedTo(SECONDS))
                .teacherId(lesson.getTeacher().getId())
                .studentId(lesson.getStudent().getId())
                .build();
    }

    private BigDecimal getPaymentForLesson(Lesson lesson) {
        var lessonDurationInMinutes = (double) lesson.getDuration();
        var lessonDurationInHour = lessonDurationInMinutes / MINUTES_IN_HOUR;
        var salaryPerHour = lesson.getTeacher().getSalaryPerHour();
        return salaryPerHour
                .multiply(BigDecimal.valueOf(lessonDurationInHour))
                .multiply(BigDecimal.valueOf(lesson.getTeacher()
                        .getPayRatio()).setScale(2, RoundingMode.DOWN));
    }

    /**
     * Метод для создания LogInfo при зачислении денег на баланс учителя
     */
    private LogInfoCreateDto createLogInfoWhenTeacherGetsPayment(Lesson lesson) {
        return LogInfoCreateDto.builder()
                .action(ActionType.ADD_MONEY_INTO_TEACHER_BALANCE)
                .description(String
                        .format("На баланс учителя %s %s было зачислено %.2f руб.",
                                lesson.getTeacher().getUserInfo().getFirstName(),
                                lesson.getTeacher().getUserInfo().getLastName(),
                                getPaymentForLesson(lesson)))
                .createdAt(now().truncatedTo(SECONDS))
                .teacherId(lesson.getTeacher().getId())
                .studentId(lesson.getStudent().getId())
                .build();
    }

    /**
     * Метод для создания LogInfo при отмене урока по уважительной причине
     */
    private LogInfoCreateDto createLogInfoWhenLessonCanceledByGoodReason(Lesson lesson) {
        return LogInfoCreateDto.builder()
                .action(ActionType.MISSING_CLASS)
                .description(String
                        .format("Ученик %s %s пропустил занятие по уважительной причине.",
                                lesson.getStudent().getUserInfo().getFirstName(),
                                lesson.getStudent().getUserInfo().getLastName()))
                .createdAt(now().truncatedTo(SECONDS))
                .teacherId(lesson.getTeacher().getId())
                .studentId(lesson.getStudent().getId())
                .build();
    }

    /**
     * Метод для создания LogInfo при отмене урока по инициативе учителя
     */
    private LogInfoCreateDto createLogInfoWhenLessonCanceledByTeacherReason(Lesson lesson) {
        return LogInfoCreateDto.builder()
                .action(ActionType.MISSING_CLASS)
                .description(String
                        .format("Занятие было отменено по инициативе учителя: %s %s.",
                                lesson.getTeacher().getUserInfo().getFirstName(),
                                lesson.getTeacher().getUserInfo().getLastName()))
                .createdAt(now().truncatedTo(SECONDS))
                .teacherId(lesson.getTeacher().getId())
                .studentId(lesson.getStudent().getId())
                .build();
    }

    /**
     * Метод для создания LogInfo при отмене урока по другим причинам
     */
    private LogInfoCreateDto createLogInfoWhenLessonCanceled(Lesson lesson) {
        return LogInfoCreateDto.builder()
                .action(ActionType.MISSING_CLASS)
                .description("Занятие было отменено.")
                .createdAt(now().truncatedTo(SECONDS))
                .teacherId(lesson.getTeacher().getId())
                .studentId(lesson.getStudent().getId())
                .build();
    }

    /**
     * Метод выполняется в случае успешно проведённого урока, т.е. учитель изменил
     * статус урока на SUCCESSFULLY_FINISHED:
     * - Списывается стоимость урока с баланса абонемента ученика;
     * - Списывается урок из абонемента;
     * - Начисляется оплата учителю за проведённый урок;
     * - Делаются записи в таблицу log_info о проведённых операциях;
     */
    private void jobInCaseLessonFinishedSuccessfully(Lesson lesson) {
        writeOffMoneyFromStudentBalance(lesson);
        subtractOneLessonFromAbonement(lesson);
        addMoneyIntoTeacherAccount(lesson);
        var studentLogInfo = createLogInfoWhenWriteOffFromStudentBalance(lesson);
        var teacherLogInfo = createLogInfoWhenTeacherGetsPayment(lesson);
        publisher.publishEvent(new EntityEvent<>(lesson, AccessType.UPDATE, studentLogInfo));
        publisher.publishEvent(new EntityEvent<>(lesson, AccessType.UPDATE, teacherLogInfo));
        abonementRepository.flush();
    }

    /**
     * Метод выполняется в случае отмены урока по уважительной причине, т.е. учитель изменил
     * статус урока на CANCELED_FOR_GOOD_REASON:
     * - Начисляется оплата учителю за проведённый урок;
     * - Делаются записи в таблицу log_info о зачислении денег на баланс учителя и об отмене урока;
     */
    private void jobInCaseLessonCanceledForGoodReason(Lesson lesson) {
        addMoneyIntoTeacherAccount(lesson);
        var teacherLogInfo = createLogInfoWhenTeacherGetsPayment(lesson);
        var godReasonLogInfo = createLogInfoWhenLessonCanceledByGoodReason(lesson);
        publisher.publishEvent(new EntityEvent<>(lesson, AccessType.UPDATE, teacherLogInfo));
        publisher.publishEvent(new EntityEvent<>(lesson, AccessType.UPDATE, godReasonLogInfo));
    }

    /**
     * Метод выполняется в случае отмены урока из-за учителя, т.е. учитель изменил
     * статус урока на CANCELED_BY_TEACHER_FAULT
     * - Делается запись в таблицу log_info об отмене урока из-за учителя.;
     */
    private void jobInCaseLessonCanceledByTeacherFault(Lesson lesson) {
        var teacherReasonLogInfo = createLogInfoWhenLessonCanceledByTeacherReason(lesson);
        publisher.publishEvent(new EntityEvent<>(lesson, AccessType.UPDATE, teacherReasonLogInfo));
        // TODO: 12/24/2023 add email sending by publisher
    }

    /**
     * Метод выполняется в случае отмены урока из-за учителя, т.е. учитель изменил
     * статус урока на CANCELED_BY_TEACHER_FAULT
     * - Делается запись в таблицу log_info об отмене урока из-за учителя.;
     */
    private void jobInCaseLessonCanceled(Lesson lesson) {
        var canceledLessonLogInfo = createLogInfoWhenLessonCanceled(lesson);
        publisher.publishEvent(new EntityEvent<>(lesson, AccessType.UPDATE, canceledLessonLogInfo));
        // TODO: 12/24/2023 add email sending and phone by publisher
    }

    private void writeOffMoneyFromStudentBalance(Lesson lesson) {
        var currentBalanceOnAbonement = lesson.getStudent().getAbonement().getBalance();
        if (currentBalanceOnAbonement.doubleValue() > 0) {
            var lessonCost = lesson.getCost();
            var subtractedBalance = currentBalanceOnAbonement.subtract(lessonCost);
            if (subtractedBalance.doubleValue() > 0) {
                lesson.getStudent().getAbonement().setBalance(subtractedBalance);
            } else {
                throw new RuntimeException("Недостаточно денег на балансе");
            }
        } else {
            throw new RuntimeException("Недостаточно денег на балансе");
        }
    }

    private void subtractOneLessonFromAbonement(Lesson lesson) {
        var currentNumberOfLessons = lesson.getStudent().getAbonement().getNumberOfLessons();
        if (currentNumberOfLessons > 0) {
            lesson.getStudent().getAbonement().setNumberOfLessons(currentNumberOfLessons - 1);
        } else {
            throw new RuntimeException("Необходимо пополнить абонемент");
        }
    }

    private void addMoneyIntoTeacherAccount(Lesson lesson) {
        var paymentForLesson = getPaymentForLesson(lesson);
        var teacherSalaryLog = SalaryLog.builder()
                .payment(paymentForLesson)
                .addedAt(LocalDateTime.now().truncatedTo(SECONDS))
                .teacher(lesson.getTeacher())
                .build();
        lesson.getTeacher().addSalaryLog(teacherSalaryLog);
        salaryLogRepository.saveAndFlush(teacherSalaryLog);
    }
}