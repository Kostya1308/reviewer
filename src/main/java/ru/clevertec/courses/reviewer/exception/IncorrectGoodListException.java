package ru.clevertec.courses.reviewer.exception;

import java.util.List;

public class IncorrectGoodListException extends FailedConsoleAppReviewException {

    private static final String EMPTY_LINE = "";
    private static final String REDUNDANT_GOODS_MESSAGE = "Лишние товары - %s.";
    private static final String REQUIRED_GOODS_MESSAGE = "Недостающие товары - %s.";
    private static final String ERROR_MESSAGE = "Некорректный список товаров. Параметры командной строки: '%s'.";

    public IncorrectGoodListException(String args, List<String> requiredGoods, List<String> redundantGoods) {
        super(String.format(ERROR_MESSAGE, args) +
                (redundantGoods.isEmpty() ? EMPTY_LINE : String.format(REDUNDANT_GOODS_MESSAGE, redundantGoods)) +
                (requiredGoods.isEmpty() ? EMPTY_LINE : String.format(REQUIRED_GOODS_MESSAGE, requiredGoods)));
    }

}
