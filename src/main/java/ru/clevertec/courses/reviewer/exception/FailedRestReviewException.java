package ru.clevertec.courses.reviewer.exception;

public class FailedRestReviewException extends RuntimeException{

    public FailedRestReviewException(String messages) {
        super(messages);
    }
}
