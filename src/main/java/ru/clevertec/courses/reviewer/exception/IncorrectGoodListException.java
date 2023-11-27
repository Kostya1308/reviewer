package ru.clevertec.courses.reviewer.exception;

import java.util.List;

public class IncorrectGoodListException extends FailedReviewException {

    private static final String EMPTY_LINE = "";
    private static final String REDUNDANT_GOODS_MESSAGE = "Лишние товары - %s .";
    private static final String REQUIRED_GOODS_MESSAGE = "\n Недостающие товары - %s .";
    private static final String ERROR_MESSAGE = "Некорректный список товаров в чеке, сформированном путем запуска " +
            "приложения командой 'java -jar <RunnerClassName>.jar %s'. ";

    public IncorrectGoodListException(String line, List<String> requiredGoods, List<String> redundantGoods) {
        super(String.format(ERROR_MESSAGE, line) +
                (redundantGoods.isEmpty() ? EMPTY_LINE : String.format(REDUNDANT_GOODS_MESSAGE, redundantGoods)) +
                (requiredGoods.isEmpty() ? EMPTY_LINE : String.format(REQUIRED_GOODS_MESSAGE, requiredGoods)));
    }

}
