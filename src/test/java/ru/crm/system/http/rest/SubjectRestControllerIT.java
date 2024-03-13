package ru.crm.system.http.rest;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ru.crm.system.integration.IntegrationTestBase;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RequiredArgsConstructor
@AutoConfigureMockMvc
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
}