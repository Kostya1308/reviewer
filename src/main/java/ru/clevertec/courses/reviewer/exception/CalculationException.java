package ru.clevertec.courses.reviewer.exception;

public class CalculationException extends FailedReviewException {

    private static final String ERROR_MESSAGE = "Некорректный результат вычислений. Параметры командной строки: '%s', " +
            " позиции: %s";

    public CalculationException(String line, String goodDescriptions) {
        super(String.format(ERROR_MESSAGE, line, goodDescriptions));
    }

}
