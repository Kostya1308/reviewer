package ru.clevertec.courses.reviewer.exception;

public class RequiredReceiptIsMissingException extends FailedConsoleAppReviewException {

    private static final String ERROR_MESSAGE = "Чек не сформирован. Параметры командной строки: '%s'";

    public RequiredReceiptIsMissingException(String args) {
        super(String.format(ERROR_MESSAGE, args));
    }

}
