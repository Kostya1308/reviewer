package ru.clevertec.courses.reviewer.validator;

import ru.clevertec.courses.reviewer.exception.IncorrectFileStructureException;

public interface CsvValidator {

    void checkNextLineIsNotHeader(String[] strings, String... headers) throws IncorrectFileStructureException;

    void checkFirstLineIsHeader(String[] strings, String... headers) throws IncorrectFileStructureException;

}
