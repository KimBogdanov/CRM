package ru.crm.system.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.crm.system.database.entity.Abonement;
import ru.crm.system.database.entity.Lesson;
import ru.crm.system.database.entity.LogInfo;
import ru.crm.system.database.entity.Student;
import ru.crm.system.database.entity.enums.ActionType;
import ru.crm.system.database.repository.LogInfoRepository;
import ru.crm.system.dto.abonement.AbonementCreatEditDto;
import ru.crm.system.dto.loginfo.LogInfoCreateDto;
import ru.crm.system.mapper.LogInfoCreateMapper;

import java.math.BigDecimal;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

import static java.time.LocalDateTime.now;
import static java.time.temporal.ChronoUnit.SECONDS;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class LogInfoService {

    private final LogInfoRepository logInfoRepository;
    private final LogInfoCreateMapper logInfoCreateMapper;
    private final SalaryService salaryService;

    @Transactional
    public Integer create(LogInfoCreateDto createDto) {
        return Optional.of(createDto)
                .map(logInfoCreateMapper::map)
                .map(logInfoRepository::save)
                .map(LogInfo::getId)
                .orElseThrow();
    }

    /**
     * Method to create basic log info for all other methods.
     */
    public LogInfoCreateDto createBaseLogInfo(Integer orderId) {
        return LogInfoCreateDto.builder()
                .createdAt(now().truncatedTo(SECONDS))
                .orderId(orderId)
                .build();
    }

    /**
     * Method for creating {@link ru.crm.system.database.entity.LogInfo} when creating and selling new {@link ru.crm.system.database.entity.Abonement}.
     */
    public LogInfoCreateDto createAbonementLogInfo(AbonementCreatEditDto createDto,
                                                   Integer adminId,
                                                   Integer orderId) {
        var logInfo = createBaseLogInfo(orderId);
        logInfo.setAction(ActionType.SALE_OF_SUBSCRIPTION);
        logInfo.setAdminId(adminId);
        logInfo.setDescription(String.format("Продан абонемент на сумму %s руб. студенту с id %d",
                createDto.balance(), createDto.studentId()));
        return logInfo;
    }

    /**
     * Method for creating {@link ru.crm.system.database.entity.LogInfo} when adding money to {@link ru.crm.system.database.entity.Abonement}.
     */
    public LogInfoCreateDto createAddMoneyLogInfo(Integer adminId,
                                                  BigDecimal moneyToAdd,
                                                  Abonement abonement,
                                                  BigDecimal newBalance) {
        return LogInfoCreateDto.builder()
                .action(ActionType.ADD_MONEY_INTO_STUDENT_BALANCE)
                .description(String.format("На счёт ученика %s %s внесено %.2f руб. Текущий балан %s руб.",
                        abonement.getStudent().getUserInfo().getFirstName(),
                        abonement.getStudent().getUserInfo().getLastName(),
                        moneyToAdd,
                        newBalance))
                .createdAt(now().truncatedTo(SECONDS))
                .adminId(adminId)
                .teacherId(abonement.getId())
                .build();
    }

    /**
     * Method for creating {@link ru.crm.system.database.entity.LogInfo} when saving new lesson into db with
     * {@link ru.crm.system.database.entity.enums.ActionType#ATTENDING_A_LESSON}
     */
    public LogInfoCreateDto createLogInfoWhenLessonAppointed(Lesson lesson) {
        return LogInfoCreateDto.builder()
                .action(ActionType.ATTENDING_A_LESSON)
                .description(String
                        .format("На %s %s назначен новый урок №%s для студентов: %s. Учитель: %s %s. Предмет: %s",
                                lesson.getDate(),
                                lesson.getTime().truncatedTo(ChronoUnit.MINUTES),
                                lesson.getId(),
                                lesson.getStudents().stream()
                                        .map(student -> String.format("%s %s",
                                                student.getUserInfo().getFirstName(),
                                                student.getUserInfo().getLastName())).toList(),
                                lesson.getTeacher().getUserInfo().getFirstName(),
                                lesson.getTeacher().getUserInfo().getLastName(),
                                lesson.getSubject().getName()))
                .createdAt(now().truncatedTo(SECONDS))
                .teacherId(lesson.getTeacher().getId())
                .lessonId(lesson.getId())
                .build();
    }

    /**
     * Method for creating {@link ru.crm.system.database.entity.LogInfo} when withdrawing money from a student's abonement
     */
    public List<LogInfoCreateDto> createLogInfosWhenWriteOffFromStudentsBalance(Lesson lesson) {
        return lesson.getStudents().stream()
                .map(student -> LogInfoCreateDto.builder()
                        .action(ActionType.WRITE_OFF_FROM_STUDENT_BALANCE)
                        .description(String
                                .format("С баланса ученика %s %s было списано %s руб.",
                                        student.getUserInfo().getFirstName(),
                                        student.getUserInfo().getLastName(),
                                        lesson.getCost()))
                        .createdAt(now().truncatedTo(SECONDS))
                        .teacherId(lesson.getTeacher().getId())
                        .studentId(student.getId())
                        .lessonId(lesson.getId())
                        .build())
                .toList();
    }

    /**
     * Method for creating {@link ru.crm.system.database.entity.LogInfo} when the lesson is canceled for a valid reason.
     */
    public List<LogInfoCreateDto> createLogInfoWhenLessonCanceledByGoodReason(Lesson lesson) {
        return lesson.getStudents().stream()
                .map(student -> LogInfoCreateDto.builder()
                        .action(ActionType.MISSING_CLASS)
                        .description(String
                                .format("Ученик %s %s пропустил занятие по уважительной причине.",
                                        student.getUserInfo().getFirstName(),
                                        student.getUserInfo().getLastName()))
                        .createdAt(now().truncatedTo(SECONDS))
                        .teacherId(lesson.getTeacher().getId())
                        .studentId(student.getId())
                        .build())
                .toList();
    }

    /**
     * Method for crating {@link ru.crm.system.database.entity.LogInfo} when adding a comment to the lesson.
     */
    public LogInfoCreateDto createCommentLogInfo(Lesson lesson, String text) {
        return LogInfoCreateDto.builder()
                .action(ActionType.COMMENT)
                .description(String.format("Учитель %s %s добавил комментарий к уроку №%d: %s.",
                        lesson.getTeacher().getUserInfo().getFirstName(),
                        lesson.getTeacher().getUserInfo().getLastName(),
                        lesson.getId(),
                        text))
                .createdAt(now().truncatedTo(SECONDS))
                .teacherId(lesson.getTeacher().getId())
                .build();
    }

    /**
     * Method for creating {@link ru.crm.system.database.entity.LogInfo} in case a lesson is canceled for other reasons.
     */
    public LogInfoCreateDto createLogInfoWhenLessonCanceled(Lesson lesson) {
        return LogInfoCreateDto.builder()
                .action(ActionType.MISSING_CLASS)
                .description("Занятие было отменено.")
                .createdAt(now().truncatedTo(SECONDS))
                .teacherId(lesson.getTeacher().getId())
                .build();
    }

    /**
     * Method for creating {@link ru.crm.system.database.entity.LogInfo} in case a lesson is canceled at the initiative of the teacher.
     */
    public LogInfoCreateDto createLogInfoWhenLessonCanceledByTeacherReason(Lesson lesson) {
        return LogInfoCreateDto.builder()
                .action(ActionType.MISSING_CLASS)
                .description(String
                        .format("Занятие было отменено по инициативе учителя: %s %s.",
                                lesson.getTeacher().getUserInfo().getFirstName(),
                                lesson.getTeacher().getUserInfo().getLastName()))
                .createdAt(now().truncatedTo(SECONDS))
                .teacherId(lesson.getTeacher().getId())
                .build();
    }

    /**
     * Method for creating {@link ru.crm.system.database.entity.LogInfo} when adding money to a teacher's balance
     */
    public LogInfoCreateDto createLogInfoWhenTeacherGetsPayment(Lesson lesson) {
        return LogInfoCreateDto.builder()
                .action(ActionType.ADD_MONEY_INTO_TEACHER_BALANCE)
                .description(String
                        .format("На баланс учителя %s %s было зачислено %.2f руб.",
                                lesson.getTeacher().getUserInfo().getFirstName(),
                                lesson.getTeacher().getUserInfo().getLastName(),
                                salaryService.getPaymentForLesson(lesson)))
                .createdAt(now().truncatedTo(SECONDS))
                .lessonId(lesson.getId())
                .teacherId(lesson.getTeacher().getId())
                .build();
    }

    /**
     * Method to create LogInfo when saving a new student to the database with
     * status TRANSFER_TO_STUDENT from {@link ru.crm.system.database.entity.enums.ActionType}.
     */
    public LogInfoCreateDto createStudentLogInfo(Integer adminId, Integer oderId, Student student) {
        return LogInfoCreateDto.builder()
                .action(ActionType.TRANSFER_TO_STUDENT)
                .description(String.format("Добавлен новый ученик - %s %s из заказа №%d",
                        student.getUserInfo().getFirstName(),
                        student.getUserInfo().getLastName(),
                        oderId))
                .createdAt(now().truncatedTo(SECONDS))
                .orderId(oderId)
                .adminId(adminId)
                .studentId(student.getId())
                .build();
    }
}