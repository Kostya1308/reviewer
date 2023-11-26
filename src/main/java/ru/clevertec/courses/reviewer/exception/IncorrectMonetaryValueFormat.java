package ru.clevertec.courses.reviewer.exception;

public class IncorrectMonetaryValueFormat extends FailedReviewException {

    public IncorrectMonetaryValueFormat(String line, String goodsDescriptions) {
        super(String.format("Некорректный формат денежных значений. Команда для запуска - " +
                "'java -jar <RunnerClassName>.jar %s. Позиции - %s", line, goodsDescriptions));
    }
}
