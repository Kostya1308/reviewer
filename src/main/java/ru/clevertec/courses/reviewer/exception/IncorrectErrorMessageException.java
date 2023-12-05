package ru.clevertec.courses.reviewer.exception;

public class IncorrectErrorMessageException extends FailedConsoleAppReviewException {

    private static final String ERROR_MESSAGE = "Некорректная причина ошибки заполнения чека, сформированного " +
            "путем запуска приложения командой 'java -jar <RunnerClassName>.jar %s";

    public IncorrectErrorMessageException(String line) {
        super(String.format(ERROR_MESSAGE, line));
    }

}
