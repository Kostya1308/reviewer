package ru.clevertec.courses.reviewer.exception;

public class FailedReviewException extends RuntimeException {

    public FailedReviewException(String message) {
        super(message);
    }

    public FailedReviewException() {

    }
}
