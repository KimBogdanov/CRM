package ru.crm.system.unit.service;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.crm.system.service.ApplicationContentService;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.mockito.Mockito.when;

@RequiredArgsConstructor
@ExtendWith(SpringExtension.class)
public class ApplicationContentServiceTest {

    private final static String TEST_PHOTO = "test_photo.jpg";

    @Mock
    private InputStream inputStream;
    @Mock
    private MockMultipartFile mockMultipartFile;
    @InjectMocks
    private ApplicationContentService applicationContentService;

    @SneakyThrows
    @AfterEach
    void clearData() {
        Files.deleteIfExists(Path.of(applicationContentService.getBucket(), TEST_PHOTO));
    }

    @Test
    void uploadImage_shouldUploadImage_whenImageExists() throws IOException {
        when(mockMultipartFile.getOriginalFilename()).thenReturn(TEST_PHOTO);
        when(inputStream.readAllBytes()).thenReturn(TEST_PHOTO.getBytes());
        when(mockMultipartFile.getInputStream()).thenReturn(inputStream);

        applicationContentService.uploadImage(mockMultipartFile);
    }
}