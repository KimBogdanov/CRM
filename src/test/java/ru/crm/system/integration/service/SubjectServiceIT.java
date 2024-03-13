package ru.crm.system.integration.service;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import ru.crm.system.integration.IntegrationTestBase;
import ru.crm.system.service.SubjectService;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@RequiredArgsConstructor
class SubjectServiceIT extends IntegrationTestBase {

    private static final Integer EXISTING_SUBJECT_ID = 1;
    private static final Integer NOT_EXISTING_SUBJECT_ID = 999;
    private final SubjectService subjectService;

    @Test
    void findAllSubjectNames_shouldReturnAllSubjectNames() {
        var allSubjectNames = subjectService.findAllSubjectNames();

        assertAll(() -> {
            assertThat(allSubjectNames).hasSize(5);
            assertThat(allSubjectNames).contains("Вокал", "Гитара", "Скрипка", "Ударные", "Фортепиано");
        });
    }

    @ParameterizedTest
    @MethodSource("getArgumentsToCheckFindAllByNameContaining")
    void findAllByNameContaining_shouldReturnSubjectNames(String partOfSubjectName, int expectedNumberOfSubject) {
        var allByNameContaining = subjectService.findAllByNameContaining(partOfSubjectName);
        assertThat(allByNameContaining).hasSize(expectedNumberOfSubject);
    }

    private static Stream<Arguments> getArgumentsToCheckFindAllByNameContaining() {
        return Stream.of(
                Arguments.of("О", 2),
                Arguments.of("а", 5),
                Arguments.of("к", 2),
                Arguments.of("гит", 1),
                Arguments.of("предмет", 0));
    }

    @Test
    void findById_shouldReturnSubjectById_ifSubjectExists() {
        var existingSubject = subjectService.findById(EXISTING_SUBJECT_ID);
        existingSubject.ifPresent(subject -> assertAll(() -> {
                    assertThat(subject.id()).isEqualTo(EXISTING_SUBJECT_ID);
                    assertThat(subject.name()).isEqualTo("Вокал");
                })
        );
    }

    @Test
    void findById_shouldReturnEmpty_ifSubjectNotExist() {
        var existingSubject = subjectService.findById(NOT_EXISTING_SUBJECT_ID);
        assertThat(existingSubject).isEmpty();
    }

    @Test
    void delete_shouldDeleteSubjectAndReturnTrue_ifSubjectExists() {
        var existingSubjectBeforeDeleting = subjectService.findById(EXISTING_SUBJECT_ID);
        existingSubjectBeforeDeleting.ifPresent(subject -> assertAll(() -> {
                    assertThat(subject.id()).isEqualTo(EXISTING_SUBJECT_ID);
                    assertThat(subject.name()).isEqualTo("Вокал");
                })
        );

        var isSubjectDeleted = subjectService.delete(EXISTING_SUBJECT_ID);
        var existingSubjectAfterDeleting = subjectService.findById(EXISTING_SUBJECT_ID);

        assertThat(isSubjectDeleted).isTrue();
        assertThat(existingSubjectAfterDeleting).isEmpty();
    }

    @Test
    void delete_shouldReturnFalse_ifSubjectNotExists() {
        var isSubjectDeleted = subjectService.delete(NOT_EXISTING_SUBJECT_ID);

        assertThat(isSubjectDeleted).isFalse();
    }
}