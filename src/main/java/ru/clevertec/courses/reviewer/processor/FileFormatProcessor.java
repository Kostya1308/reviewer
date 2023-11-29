package ru.clevertec.courses.reviewer.processor;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.tika.detect.DefaultDetector;
import org.apache.tika.detect.Detector;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.mime.MediaType;
import org.springframework.stereotype.Component;
import ru.clevertec.courses.reviewer.dto.TaskDto;
import ru.clevertec.courses.reviewer.exception.IncorrectFileFormatException;

import static ru.clevertec.courses.reviewer.util.FileUtil.*;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;

@Slf4j
@Component
@RequiredArgsConstructor
public class FileFormatProcessor extends AbstractCheckingProcessor {

    @Override
    public void check(TaskDto taskDto) {
        log.info("Running the file format check filter");

        taskDto.getCorrectFiles()
                .forEach(this::checkCsvFileFormat);

        log.info("The file format check filter has been successfully passed");
    }

    @SneakyThrows
    private void checkCsvFileFormat(File file) throws IncorrectFileFormatException {
        try (BufferedInputStream bufferedInputStream = new BufferedInputStream(new FileInputStream(file))) {
            Detector detector = new DefaultDetector();
            Metadata metadata = new Metadata();
            MediaType mediaType = detector.detect(bufferedInputStream, metadata);

            if (!MediaType.TEXT_PLAIN.getType().equals(mediaType.getType())) {
                throw new IncorrectFileFormatException(substringToDot(file.getName()));
            }
        }
    }

    @Override
    public int getOrder() {
        return 10;
    }
}
