package ru.clevertec.courses.reviewer.exception;

public class RedundantReceiptIsPresentException extends RuntimeException {

    public RedundantReceiptIsPresentException() {
        super("Среди чеков, представленных для проверки, присутствует лишний");
    }
}
