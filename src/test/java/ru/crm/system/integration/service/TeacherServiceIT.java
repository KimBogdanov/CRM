package ru.crm.system.integration.service;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import ru.crm.system.integration.IntegrationTestBase;
import ru.crm.system.service.TeacherService;

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
}