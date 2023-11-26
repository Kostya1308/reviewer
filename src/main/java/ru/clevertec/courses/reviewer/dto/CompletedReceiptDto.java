package ru.clevertec.courses.reviewer.dto;

import com.opencsv.bean.CsvBindByName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import static ru.clevertec.courses.reviewer.constant.Constant.DATE_HEADER;
import static ru.clevertec.courses.reviewer.constant.Constant.DESCRIPTION_HEADER;
import static ru.clevertec.courses.reviewer.constant.Constant.DISCOUNT_CARD_HEADER;
import static ru.clevertec.courses.reviewer.constant.Constant.DISCOUNT_HEADER;
import static ru.clevertec.courses.reviewer.constant.Constant.DISCOUNT_PERCENTAGE_HEADER;
import static ru.clevertec.courses.reviewer.constant.Constant.PRICE_HEADER;
import static ru.clevertec.courses.reviewer.constant.Constant.QTY_HEADER;
import static ru.clevertec.courses.reviewer.constant.Constant.TIME_HEADER;
import static ru.clevertec.courses.reviewer.constant.Constant.TOTAL_DISCOUNT_HEADER;
import static ru.clevertec.courses.reviewer.constant.Constant.TOTAL_HEADER;
import static ru.clevertec.courses.reviewer.constant.Constant.TOTAL_PRICE_HEADER;
import static ru.clevertec.courses.reviewer.constant.Constant.TOTAL_WITH_DISCOUNT_HEADER;

import java.util.List;
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class CompletedReceiptDto implements ReceiptDto{

    private DateTimeInfo dateTimeInfo;
    private List<GoodsInfo> goodsInfoList;
    private DiscountInfo discountInfo;
    private TotalInfo totalInfo;

    public  interface Body {
    }

    @Data
    @EqualsAndHashCode
    public static class DateTimeInfo implements Body {

        @CsvBindByName(column = DATE_HEADER)
        private String date;

        @CsvBindByName(column = TIME_HEADER)
        private String time;
    }

    @Data
    @EqualsAndHashCode
    public static class GoodsInfo implements Body {

        @CsvBindByName(column = QTY_HEADER)
        private String quantity;

        @CsvBindByName(column = PRICE_HEADER)
        private String price;

        @CsvBindByName(column = TOTAL_HEADER)
        private String total;

        @CsvBindByName(column = DISCOUNT_HEADER)
        private String discount;

        @CsvBindByName(column = DESCRIPTION_HEADER)
        private String description;
    }

    @Data
    @EqualsAndHashCode
    public static class DiscountInfo implements Body {

        @CsvBindByName(column = DISCOUNT_CARD_HEADER)
        private String discountCard;

        @CsvBindByName(column = DISCOUNT_PERCENTAGE_HEADER)
        private String discountPercentage;
    }

    @Data
    @EqualsAndHashCode
    public static class TotalInfo implements Body {

        @CsvBindByName(column = TOTAL_PRICE_HEADER)
        private String totalPrice;

        @CsvBindByName(column = TOTAL_DISCOUNT_HEADER)
        private String totalDiscount;

        @CsvBindByName(column = TOTAL_WITH_DISCOUNT_HEADER)
        private String totalWithDiscount;
    }

}
