package ru.clevertec.courses.reviewer.validator.impl;

import org.springframework.stereotype.Component;
import ru.clevertec.courses.reviewer.exception.IncorrectFileStructureException;
import ru.clevertec.courses.reviewer.validator.CsvValidator;

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
        Arrays.stream(headers)
                .filter(header -> Arrays.stream(lineArray).noneMatch(item -> Objects.equals(item, header)))
                .findAny()
                .ifPresent(header -> {
                    throw new IncorrectFileStructureException();
                });
    }

}
