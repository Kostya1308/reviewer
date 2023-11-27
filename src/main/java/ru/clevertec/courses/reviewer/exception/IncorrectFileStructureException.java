package ru.clevertec.courses.reviewer.exception;

public class IncorrectFileStructureException extends FailedReviewException {

    private static final String ERROR_MESSAGE = "Некорректная структура файла, сформированного путем запуска " +
            "приложения командой 'java -jar <RunnerClassName>.jar %s'";

    public IncorrectFileStructureException(String line) {
        super(String.format(ERROR_MESSAGE, line));
    }

}
