package ru.crm.system.service;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;

import static java.nio.file.StandardOpenOption.CREATE;
import static java.nio.file.StandardOpenOption.TRUNCATE_EXISTING;

@Data
@Service
@RequiredArgsConstructor
public class ApplicationContentService {

//    @Value("${spring.servlet.multipart.location}")
    private final String bucket = ".\\src\\main\\resources\\images\\";

//    @Value("${my-app.content.no-image-available}")
    private final String contentNotFoundImage = ".\\src\\main\\resources\\images\\NoImageAvailable.jpg";

    @SneakyThrows
    public void uploadImage(MultipartFile content) {
        if (content != null) {
            var inputStream = content.getInputStream();
            var fullImagePath = Path.of(bucket, content.getOriginalFilename());
            if (!Objects.equals(content.getOriginalFilename(), "")) {
                try (inputStream) {
                    Files.createDirectories(fullImagePath.getParent());
                    Files.write(fullImagePath, inputStream.readAllBytes(), CREATE, TRUNCATE_EXISTING);
                }
            }
        }
    }
}