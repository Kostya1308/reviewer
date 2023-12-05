package ru.clevertec.courses.reviewer.exception;

public class FailedConsoleAppReviewException extends RuntimeException {

    public FailedConsoleAppReviewException(String message) {
        super(message);
    }

    public FailedConsoleAppReviewException() {
    }
}
