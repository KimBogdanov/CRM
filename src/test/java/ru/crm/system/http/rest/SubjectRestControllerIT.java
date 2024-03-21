package ru.crm.system.http.rest;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ru.crm.system.integration.IntegrationTestBase;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RequiredArgsConstructor
class SubjectRestControllerIT extends IntegrationTestBase {

    private static final String SUBJECT_BASE_URL = "/api/v1/subjects";

    private MockMvc mockMvc;
    private final SubjectRestController subjectRestController;

    @BeforeEach
    void init() {
        mockMvc = MockMvcBuilders.standaloneSetup(subjectRestController)
                .alwaysDo(print())
                .build();
    }

    @Test
    void findAllBySubjectName_shouldReturn200() throws Exception {
        mockMvc.perform(get(SUBJECT_BASE_URL))
                .andExpect(status().isOk());
    }

    @ParameterizedTest
    @ValueSource(strings = {"Вокал", "Гитара", "Скрипка", "Ударные", "Фортепиано"})
    void findByName_shouldFindSubjectByNameAndReturn200_whenSubjectExists(String name) throws Exception {
        mockMvc.perform(get(SUBJECT_BASE_URL + "/by-name").queryParam("name", name))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON));
    }

    @ParameterizedTest
    @ValueSource(strings = {"Барабан", "Флейта", "Балалайка", "Тромбон"})
    void findByName_shouldReturn404_whenSubjectNotExist(String name) throws Exception {
        mockMvc.perform(get(SUBJECT_BASE_URL + "/by-name").queryParam("name", name))
                .andExpect(status().isNotFound());
    }

    @ParameterizedTest
    @ValueSource(strings = {"O", "a", "к", "гит" ,"предмет"})
    void findAllByPartOfName_shouldReturnSubjectListByPartOfName(String partOfName) throws Exception {
        mockMvc.perform(get(SUBJECT_BASE_URL + "/by-part-of-name").param("partOfName", partOfName))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON));
    }
}