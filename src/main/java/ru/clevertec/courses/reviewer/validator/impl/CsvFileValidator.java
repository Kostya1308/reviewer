package ru.clevertec.courses.reviewer.validator.impl;

import lombok.SneakyThrows;
import org.apache.tika.detect.DefaultDetector;
import org.apache.tika.detect.Detector;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.mime.MediaType;
import org.springframework.stereotype.Component;
import ru.clevertec.courses.reviewer.exception.IncorrectFileStructureException;
import ru.clevertec.courses.reviewer.validator.CsvValidator;

import static ru.clevertec.courses.reviewer.constant.Constant.SEPARATOR_STRING;

import java.io.ByteArrayInputStream;
import java.util.Arrays;
import java.util.Objects;

@Component
public class CsvFileValidator implements CsvValidator {

    public void checkNextLineIsNotHeader(String[] lineArray, String... headers) throws IncorrectFileStructureException {
        Arrays.stream(headers)
                .filter(header -> Arrays.toString(lineArray).contains(header))
                .findAny()
                .ifPresent(header -> {
                    throw new IncorrectFileStructureException();
                });
    }

    public void checkFirstLineIsHeader(String[] lineArray, String... headers) throws IncorrectFileStructureException {
        String[] firstLines = lineArray[0].split(SEPARATOR_STRING);

        Arrays.stream(headers)
                .filter(header -> Arrays.stream(firstLines).noneMatch(item -> Objects.equals(item, header)))
                .findAny()
                .ifPresent(header -> {
                    throw new IncorrectFileStructureException();
                });
    }

    @SneakyThrows
    public static String checkCsvFileFormat(byte[] file) {
        Detector detector = new DefaultDetector();
        Metadata metadata = new Metadata();

        ByteArrayInputStream inputStream = new ByteArrayInputStream(file);
        MediaType mediaType = detector.detect(inputStream, metadata);

        return mediaType.getSubtype();
    }

}
