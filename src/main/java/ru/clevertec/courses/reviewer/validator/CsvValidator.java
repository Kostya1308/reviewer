package ru.clevertec.courses.reviewer.validator;

import ru.clevertec.courses.reviewer.exception.IncorrectFileStructureException;

import java.util.List;

public interface CsvValidator {

    void checkNextLineIsNotHeader(String[] lineArray, List<String> headers) throws IncorrectFileStructureException;

    void checkFirstLineIsHeader(String[] lineArray, List<String> headers) throws IncorrectFileStructureException;

}
