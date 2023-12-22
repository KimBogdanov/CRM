package ru.crm.system.service;

import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.crm.system.database.entity.Student;
import ru.crm.system.database.entity.enums.ActionType;
import ru.crm.system.database.entity.enums.OrderStatus;
import ru.crm.system.database.repository.OrderRepository;
import ru.crm.system.database.repository.StudentRepository;
import ru.crm.system.dto.loginfo.LogInfoCreateDto;
import ru.crm.system.dto.student.StudentCreateEditDto;
import ru.crm.system.dto.student.StudentReadDto;
import ru.crm.system.listener.entity.AccessType;
import ru.crm.system.listener.entity.EntityEvent;
import ru.crm.system.mapper.student.StudentCreateEditMapper;
import ru.crm.system.mapper.student.StudentReadMapper;

import java.util.Optional;

import static java.time.LocalDateTime.now;
import static java.time.temporal.ChronoUnit.SECONDS;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class StudentService {

    private final StudentRepository studentRepository;
    private final StudentCreateEditMapper studentCreateEditMapper;
    private final StudentReadMapper studentReadMapper;
    private final ApplicationEventPublisher publisher;
    private final OrderRepository orderRepository;

    @Transactional
    public StudentReadDto create(Integer orderId,
                                 StudentCreateEditDto creatDto,
                                 Integer adminId) {
        return Optional.of(creatDto)
                .map(studentCreateEditMapper::map)
                .map(studentRepository::save)
                .map(student -> {
                    var logInfo = createStudentLogInfo(adminId, orderId, student);
                    publisher.publishEvent(new EntityEvent<>(student, AccessType.CREATE, logInfo));
                    orderRepository.findById(orderId)
                            .ifPresent(order -> order.setStatus(OrderStatus.SUCCESSFULLY_COMPLETED));
                    return studentReadMapper.map(student);
                })
                .orElseThrow();
    }

    /**
     * Метод для создания базового LogInfo для всех методов
     */
    private LogInfoCreateDto createLogInfo(Integer studentId) {
        return LogInfoCreateDto.builder()
                .createdAt(now().truncatedTo(SECONDS))
                .orderId(studentId)
                .build();
    }

    /**
     * Метод для создания LogInfo при сохранении нового студента в базу данных
     * со статусом TRANSFER_TO_STUDENT из {@link ActionType} .
     */
    private LogInfoCreateDto createStudentLogInfo(Integer adminId, Integer oderId, Student student) {
        var logInfo = createLogInfo(student.getId());
        logInfo.setAdminId(adminId);
        logInfo.setOrderId(oderId);
        logInfo.setDescription(String.format("Добавлен новый ученик - %s %s из заказа №%d",
                student.getUserInfo().getFirstName(),
                student.getUserInfo().getLastName(),
                oderId));
        logInfo.setAction(ActionType.TRANSFER_TO_STUDENT);
        return logInfo;
    }
}