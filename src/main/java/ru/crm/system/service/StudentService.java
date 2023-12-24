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

import javax.persistence.EntityNotFoundException;
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

    public StudentReadDto findById(Integer id) {
        return studentRepository.findById(id)
                .map(studentReadMapper::map)
                .orElseThrow(EntityNotFoundException::new);
    }

    /**
     * Метод для создания LogInfo при сохранении нового студента в базу данных
     * со статусом TRANSFER_TO_STUDENT из {@link ActionType} .
     */
    private LogInfoCreateDto createStudentLogInfo(Integer adminId, Integer oderId, Student student) {
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