package ru.clevertec.courses.reviewer.exception;

public class IncorrectErrorMessageException extends FailedReviewException {

    public IncorrectErrorMessageException(String line) {
        super(String.format("Некорректная причина ошибки заполнения чека, сформированного путем запуска приложения командой " +
                "'java -jar <RunnerClassName>.jar %s", line));
    }
}
