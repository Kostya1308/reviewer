package ru.clevertec.courses.reviewer.exception;

public class ReviewerNotFoundException extends RuntimeException {

    public ReviewerNotFoundException(String branchName) {
        super(String.format("Service for reviewing the %s branch not found", branchName));
    }
}
