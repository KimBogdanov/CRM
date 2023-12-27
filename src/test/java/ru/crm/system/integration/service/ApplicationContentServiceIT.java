package ru.crm.system.integration.service;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import ru.crm.system.service.ApplicationContentService;

import java.nio.file.Files;
import java.nio.file.Path;

import static org.assertj.core.api.Assertions.assertThat;

@RequiredArgsConstructor
@SpringBootTest
public class ApplicationContentServiceIT {

    private final String bucket = ".\\src\\main\\resources\\images\\";
    private final ApplicationContentService applicationContentService;
    private MockMultipartFile dummyImage;
    private Path fullPathToImage;

    @BeforeEach
    void initFileAndPath() {
        var image = "ImageToTestContentMethods.jpg";
        dummyImage = new MockMultipartFile(image, image, MediaType.APPLICATION_OCTET_STREAM_VALUE, image.getBytes());
        fullPathToImage = Path.of(bucket, dummyImage.getName());
    }

    @SneakyThrows
    @AfterEach
    void clearData() {
        Files.deleteIfExists(fullPathToImage);
    }

    @Test
    void uploadImage_shouldUploadImage_whenImageIsValid() {
        var isFileExist = Files.exists(fullPathToImage);
        assertThat(isFileExist).isFalse();

        applicationContentService.uploadImage(dummyImage);
        var isFileUploaded = Files.exists(fullPathToImage);

        assertThat(isFileUploaded).isTrue();
    }
}