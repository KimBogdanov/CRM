package ru.crm.system.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.crm.system.database.entity.Lesson;
import ru.crm.system.database.entity.SalaryLog;
import ru.crm.system.database.repository.SalaryLogRepository;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;

import static java.time.temporal.ChronoUnit.SECONDS;

@Service
@RequiredArgsConstructor
public class SalaryService {

    private static final double MINUTES_IN_HOUR = 60.0;

    private final SalaryLogRepository salaryLogRepository;

    public BigDecimal getPaymentForLesson(Lesson lesson) {
        var lessonDurationInMinutes = (double) lesson.getDuration();
        var lessonDurationInHour = lessonDurationInMinutes / MINUTES_IN_HOUR;
        var salaryPerHour = lesson.getTeacher().getSalaryPerHour();
        return salaryPerHour
                .multiply(BigDecimal.valueOf(lessonDurationInHour))
                .multiply(BigDecimal.valueOf(lesson.getTeacher()
                        .getPayRatio()).setScale(2, RoundingMode.DOWN));
    }

    public void addMoneyIntoTeacherAccount(Lesson lesson) {
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