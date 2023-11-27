package ru.clevertec.courses.reviewer.exception;

public class IncorrectMonetaryValueFormat extends FailedReviewException {

    private static final String ERROR_MESSAGE = "Некорректный формат денежных значений. Команда для запуска - " +
            "'java -jar <RunnerClassName>.jar %s. Позиции - %s";

    public IncorrectMonetaryValueFormat(String line, String goodsDescriptions) {
        super(String.format(ERROR_MESSAGE, line, goodsDescriptions));
    }

}
