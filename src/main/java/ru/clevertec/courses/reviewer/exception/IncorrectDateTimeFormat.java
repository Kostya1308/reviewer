package ru.clevertec.courses.reviewer.exception;

public class IncorrectDateTimeFormat extends FailedReviewException{

    public IncorrectDateTimeFormat(String line) {
        super(String.format("Некорректный формат даты/времени чека, сформированного путем запуска приложения командой " +
                "'java -jar <RunnerClassName>.jar %s", line));
    }
}
