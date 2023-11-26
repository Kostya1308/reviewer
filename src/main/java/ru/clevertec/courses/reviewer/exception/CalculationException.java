package ru.clevertec.courses.reviewer.exception;

public class CalculationException extends FailedReviewException{

    public CalculationException(String line, String goodsDescriptions) {
        super(String.format("Некорректный результат вычислений. Команда для запуска - " +
                "'java -jar <RunnerClassName>.jar %s. Позиции - %s", line, goodsDescriptions));
    }
}
