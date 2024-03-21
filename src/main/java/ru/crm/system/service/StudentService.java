package ru.crm.system.service;

import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.crm.system.database.entity.enums.OrderStatus;
import ru.crm.system.database.repository.OrderRepository;
import ru.crm.system.database.repository.StudentRepository;
import ru.crm.system.dto.student.StudentCreateEditDto;
import ru.crm.system.dto.student.StudentReadDto;
import ru.crm.system.listener.entity.AccessType;
import ru.crm.system.listener.entity.EntityEvent;
import ru.crm.system.mapper.student.StudentCreateEditMapper;
import ru.crm.system.mapper.student.StudentReadMapper;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class StudentService {

    private final StudentCreateEditMapper studentCreateEditMapper;
    private final StudentRepository studentRepository;
    private final LogInfoService logInfoService;
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
                    var logInfo = logInfoService.createStudentLogInfo(adminId, orderId, student);
                    publisher.publishEvent(new EntityEvent<>(student, AccessType.CREATE, logInfo));
                    orderRepository.findById(orderId)
                            .ifPresent(order -> order.setStatus(OrderStatus.SUCCESSFULLY_COMPLETED));
                    return studentReadMapper.map(student);
                })
                .orElseThrow();
    }

    public Optional<StudentReadDto> findById(Integer id) {
        return studentRepository.findById(id)
                .map(studentReadMapper::map);
    }

    public List<StudentReadDto> findAllBySubject(String subject) {
        return studentRepository.findAllBySubject(subject).stream()
                .map(studentReadMapper::map)
                .toList();
    }
}