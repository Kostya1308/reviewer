package ru.clevertec.courses.reviewer.exception;

public class IncorrectFileStructureException extends FailedReviewException{

    public IncorrectFileStructureException(String line) {
        super(String.format("Некорректная структура файла, сформированного путем запуска приложения командой " +
                "'java -jar <RunnerClassName>.jar %s'", line));
    }
}
