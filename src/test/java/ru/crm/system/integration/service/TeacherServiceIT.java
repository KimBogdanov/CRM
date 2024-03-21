package ru.crm.system.integration.service;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import ru.crm.system.database.entity.enums.TeacherStatus;
import ru.crm.system.dto.teacher.TeacherCreateEditDto;
import ru.crm.system.integration.IntegrationTestBase;
import ru.crm.system.service.TeacherService;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@RequiredArgsConstructor
public class TeacherServiceIT extends IntegrationTestBase {

    private static final Integer EXISTING_TEACHER_ID = 1;
    private static final Integer NOT_EXISTING_TEACHER_ID = 999;
    private final TeacherService teacherService;

    @Test
    void findById_shouldReturnTeacherById_ifTeacherExists() {
        var existingTeacher = teacherService.findById(EXISTING_TEACHER_ID);

        assertThat(existingTeacher).isPresent();
        existingTeacher.ifPresent(teacher -> assertAll(() -> {
            assertThat(teacher.id()).isEqualTo(EXISTING_TEACHER_ID);
            assertThat(teacher.firstName()).isEqualTo("Наталья");
            assertThat(teacher.lastName()).isEqualTo("Петрова");
            assertThat(teacher.phone()).isEqualTo("8-88-88-88-8");
            assertThat(teacher.email()).isEqualTo("natalya@Gmail.com");
            assertThat(teacher.salaryPerHour().toString()).isEqualTo("900.00");
        }));
    }

    @Test
    void findById_shouldReturnEmpty_ifTeacherNotExist() {
        var existingTeacher = teacherService.findById(NOT_EXISTING_TEACHER_ID);

        assertThat(existingTeacher).isEmpty();
    }

    @Test
    void create_shouldCreateTeacherAndSaveItIntoDb_whenCreateDtoValid() {
        var teacherCreateDto = getTeacherCreateDto();
        var createdTeacher = teacherService.create(teacherCreateDto);

        assertAll(() -> {
            assertThat(createdTeacher.id()).isPositive();
            assertThat(createdTeacher.firstName()).isEqualTo("TestFirstName");
            assertThat(createdTeacher.lastName()).isEqualTo("TestLastName");
            assertThat(createdTeacher.phone()).isEqualTo("8-9-778-89-65");
            assertThat(createdTeacher.email()).isEqualTo("check@gmail.com");
            assertThat(createdTeacher.status().name()).isEqualTo(TeacherStatus.ACTIVE.name());
            assertThat(createdTeacher.subjects().size()).isEqualTo(2);
        });
    }

    @Test
    void delete_shouldDeleteTeacher_ifTeacherExists() {
        var existingTeacher = teacherService.findById(EXISTING_TEACHER_ID);
        assertThat(existingTeacher).isPresent();

        var isTeacherDeleted = teacherService.delete(EXISTING_TEACHER_ID);
        var teacherAfterDeleting = teacherService.findById(EXISTING_TEACHER_ID);

        assertThat(isTeacherDeleted).isTrue();
        assertThat(teacherAfterDeleting).isEmpty();
    }

    private TeacherCreateEditDto getTeacherCreateDto() {
        return TeacherCreateEditDto.builder()
                .firstName("TestFirstName")
                .lastName("TestLastName")
                .phone("8-9-778-89-65")
                .email("check@gmail.com")
                .rawPassword("123")
                .status(TeacherStatus.ACTIVE)
                .subjects(List.of("Вокал", "Гитара"))
                .salaryPerHour(BigDecimal.valueOf(1200))
                .build();
    }
}