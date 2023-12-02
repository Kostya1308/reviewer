package ru.clevertec.courses.reviewer.constant;

import java.util.List;

public class Constant {

    private Constant() {
    }

    public static final String TIME_HEADER = "Time";
    public static final String DATE_HEADER = "Date";

    public static final String QTY_HEADER = "QTY";
    public static final String PRICE_HEADER = "PRICE";
    public static final String TOTAL_HEADER = "TOTAL";
    public static final String DISCOUNT_HEADER = "DISCOUNT";
    public static final String DESCRIPTION_HEADER = "DESCRIPTION";

    public static final String DISCOUNT_CARD_HEADER = "DISCOUNT CARD";
    public static final String DISCOUNT_PERCENTAGE_HEADER = "DISCOUNT PERCENTAGE";

    public static final String TOTAL_PRICE_HEADER = "TOTAL PRICE";
    public static final String TOTAL_DISCOUNT_HEADER = "TOTAL DISCOUNT";
    public static final String TOTAL_WITH_DISCOUNT_HEADER = "TOTAL WITH DISCOUNT";

    public static final List<String> dateTimeHeaders = List.of(TIME_HEADER, DATE_HEADER);
    public static final List<String> discountHeaders = List.of(DISCOUNT_CARD_HEADER, DISCOUNT_PERCENTAGE_HEADER);
    public static final List<String> totalHeaders =
            List.of(TOTAL_PRICE_HEADER, TOTAL_DISCOUNT_HEADER, TOTAL_WITH_DISCOUNT_HEADER);
    public static final List<String> goodsHeaders =
            List.of(QTY_HEADER, PRICE_HEADER, TOTAL_HEADER, DISCOUNT_HEADER, DESCRIPTION_HEADER);

    public static final String ERROR_HEADER = "ERROR";

    public static final String DOT = ".";
    public static final String APPENDER = "\n";
    public static final String SEPARATOR_STRING = ";";
    public static final Character SEPARATOR_CHAR = ';';

    public static final Integer REQUIRED_NUMBER_ONE = 1;
    public static final Integer DEFAULT_NUMBER_LINES_TO_SKIP = 2;

    public static final String[] EMPTY_STRING_ARRAY = new String[0];

}
