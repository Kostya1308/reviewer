package ru.clevertec.courses.reviewer.validator.impl;

import org.springframework.stereotype.Component;
import ru.clevertec.courses.reviewer.exception.IncorrectFileStructureException;
import ru.clevertec.courses.reviewer.validator.CsvValidator;

import static ru.clevertec.courses.reviewer.constant.Constant.INCORRECT_STRUCTURE_MESSAGE;

import java.util.Arrays;

@Component
public class CsvFileValidator implements CsvValidator {

    public void checkNextLineIsNotHeader(String[] strings, String... headers) {
        boolean nextLineIsNotHeader = Arrays.stream(headers)
                .anyMatch(header -> Arrays.toString(strings).contains(header));

        if (nextLineIsNotHeader) {
            throw new IncorrectFileStructureException(INCORRECT_STRUCTURE_MESSAGE);
        }
    }

    public void checkFirstLineIsHeader(String[] strings, String... headers) {
        boolean firstLineIsHeader = Arrays.stream(headers)
                .allMatch(header -> Arrays.toString(strings).contains(header));

        if (!firstLineIsHeader) {
            throw new IncorrectFileStructureException(INCORRECT_STRUCTURE_MESSAGE);
        }
    }

}
