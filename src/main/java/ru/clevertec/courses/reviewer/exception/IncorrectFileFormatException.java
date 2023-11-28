package ru.clevertec.courses.reviewer.exception;

public class IncorrectFileFormatException extends FailedReviewException {

    private static final String ERROR_MESSAGE = "Некорректный формат файла. Параметры командной строки: '%s'";

    public IncorrectFileFormatException(String args) {
        super(String.format(ERROR_MESSAGE, args));
    }

}
