package ru.clevertec.courses.reviewer.exception;

public class RequiredReceiptIsMissingException extends FailedReviewException {

    private static final String ERROR_MESSAGE = "Отсутствует чек, который должен был быть сформирован после " +
            "выполения команды 'java -jar <RunnerClassName>.jar %s'";

    public RequiredReceiptIsMissingException(String line) {
        super(String.format(ERROR_MESSAGE, line));
    }

}
