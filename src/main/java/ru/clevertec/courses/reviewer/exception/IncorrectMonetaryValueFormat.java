package ru.clevertec.courses.reviewer.exception;

public class IncorrectMonetaryValueFormat extends FailedConsoleAppReviewException {

    private static final String ERROR_MESSAGE = "Некорректный формат денежных значений. " +
            "Параметры командной строки: '%s', позиции: %s";

    public IncorrectMonetaryValueFormat(String args, String goodDescriptions) {
        super(String.format(ERROR_MESSAGE, args, goodDescriptions));
    }

}
