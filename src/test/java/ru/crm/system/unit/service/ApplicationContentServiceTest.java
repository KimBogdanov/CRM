package ru.crm.system.unit.service;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.crm.system.service.ApplicationContentService;

import java.io.IOException;
import java.io.InputStream;

import static org.mockito.Mockito.when;

@RequiredArgsConstructor
@ExtendWith(SpringExtension.class)
public class ApplicationContentServiceTest {

    @Mock
    private InputStream inputStream;

    @Mock
    private MockMultipartFile mockMultipartFile;

    @InjectMocks
    private ApplicationContentService applicationContentService;

    @Test
    void uploadImage_shouldUploadImage_whenImageExists() throws IOException {
        when(mockMultipartFile.getOriginalFilename()).thenReturn("test_photo.jpg");
        when(inputStream.readAllBytes()).thenReturn("test_photo.jpg".getBytes());
        when(mockMultipartFile.getInputStream()).thenReturn(inputStream);

        applicationContentService.uploadImage(mockMultipartFile);
    }
}