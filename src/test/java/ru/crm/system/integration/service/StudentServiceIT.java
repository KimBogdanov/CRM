package ru.crm.system.integration.service;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import ru.crm.system.integration.IT;
import ru.crm.system.service.StudentService;

import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

@IT
@RequiredArgsConstructor
public class StudentServiceIT {

    private final StudentService studentService;

    @ParameterizedTest
    @MethodSource("getArgumentsForFindAllBySubject")
    void findAllBySubject_shouldReturnStudentsBySubject(String subject, List<String> expectedStudentFullName) {
        var actualStudents = studentService.findAllBySubject(subject);

        var actualStudentFullNames = actualStudents.stream()
                .map(student -> String.format("%s %s", student.firstName(), student.lastName()))
                .toList();

        assertThat(actualStudents).hasSize(expectedStudentFullName.size());
        assertThat(actualStudentFullNames).containsExactlyElementsOf(expectedStudentFullName);
    }

    static Stream<Arguments> getArgumentsForFindAllBySubject() {
        return Stream.of(
                Arguments.of("Вокал", List.of("Андрей Иванов")),
                Arguments.of("Гитара", List.of("Павел Петров")),
                Arguments.of("Скрипка", List.of("Катя Степанова")),
                Arguments.of("Ударные", List.of("Маша Андреева")),
                Arguments.of("Фортепиано", List.of("Егор Афанасьев"))
        );
    }
}