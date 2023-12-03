package ru.clevertec.courses.reviewer.exception;

import java.util.List;

public class DuplicateGoodsException extends FailedReviewException {

    private static final String ERROR_MESSAGE = "В списке товаров присуствуют дубликаты. " +
            "Параметры командной строки: '%s'. Позиции: %s";

    public DuplicateGoodsException(String line, List<String> duplicates) {
        super(String.format(ERROR_MESSAGE, line, duplicates.toString()));
    }
}
