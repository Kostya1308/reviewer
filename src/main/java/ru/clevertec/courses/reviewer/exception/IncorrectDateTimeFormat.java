package ru.clevertec.courses.reviewer.exception;

public class IncorrectDateTimeFormat extends FailedConsoleAppReviewException {

    private static final String ERROR_MESSAGE = "Некорректный формат даты/времени чека. " +
            "Параметры командной строки:  '%s'.";

    public IncorrectDateTimeFormat(String args) {
        super(String.format(ERROR_MESSAGE, args));
    }
}
