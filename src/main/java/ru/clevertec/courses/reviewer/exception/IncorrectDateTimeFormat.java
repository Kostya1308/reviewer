package ru.clevertec.courses.reviewer.exception;

public class IncorrectDateTimeFormat extends FailedReviewException {

    private static final String ERROR_MESSAGE = "Некорректный формат даты/времени чека, сформированного путем " +
            "запуска приложения командой 'java -jar <RunnerClassName>.jar %s";

    public IncorrectDateTimeFormat(String line) {
        super(String.format(ERROR_MESSAGE, line));
    }
}
