package ru.clevertec.courses.reviewer.exception;

public class CalculationException extends FailedReviewException {

    private static final String ERROR_MESSAGE = "Некорректный результат вычислений. Команда для запуска - " +
            "'java -jar <RunnerClassName>.jar %s. Позиции - %s";

    public CalculationException(String line, String goodsDescriptions) {
        super(String.format(ERROR_MESSAGE, line, goodsDescriptions));
    }

}
