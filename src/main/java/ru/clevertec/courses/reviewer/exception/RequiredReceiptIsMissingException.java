package ru.clevertec.courses.reviewer.exception;

public class RequiredReceiptIsMissingException extends FailedReviewException {

    public RequiredReceiptIsMissingException(String line) {
        super(String.format("Отсутствует чек, который должен был быть сформирован после выполения команды " +
                "'java -jar <RunnerClassName>.jar %s'", line));
    }
}
