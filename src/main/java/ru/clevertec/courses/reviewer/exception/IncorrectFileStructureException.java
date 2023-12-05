package ru.clevertec.courses.reviewer.exception;

public class IncorrectFileStructureException extends FailedConsoleAppReviewException {

    private static final String ERROR_MESSAGE = "Некорректная структура файла. Параметры командной строки: '%s'.";

    public IncorrectFileStructureException(String args) {
        super(String.format(ERROR_MESSAGE, args));
    }

    public IncorrectFileStructureException() {
        super();
    }
}
