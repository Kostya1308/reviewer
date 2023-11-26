package ru.clevertec.courses.reviewer.validator;

public interface CsvValidator {

    void checkNextLineIsNotHeader(String[] strings, String... headers);

    void checkFirstLineIsHeader(String[] strings, String... headers);

}
