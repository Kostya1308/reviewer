package ru.clevertec.courses.reviewer.checker.console;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.clevertec.courses.reviewer.constant.Constant;
import ru.clevertec.courses.reviewer.dto.CompletedReceiptDto;
import ru.clevertec.courses.reviewer.dto.TaskDto;
import ru.clevertec.courses.reviewer.exception.IncorrectDateTimeFormat;
import ru.clevertec.courses.reviewer.exception.IncorrectMonetaryValueFormat;
import ru.clevertec.courses.reviewer.parser.FileParser;

import static java.util.function.Predicate.not;
import static ru.clevertec.courses.reviewer.util.FileUtil.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.regex.Pattern;

@Slf4j
@Component
@RequiredArgsConstructor
public class DataFormatChecker extends AbstractConsoleChecker {

    private static final String TIME_PATTERN = "HH:mm:ss";
    private static final String DATE_PATTERN = "dd.MM.yyyy";
    private static final String MONETARY_VALUE_PATTERN = "\\d+\\.\\d{2}\\$";

    private final FileParser fileParser;

    @Override
    public void check(TaskDto taskDto) {
        log.info("Running the data format check filter");

        var receiptDtoToReviewMap = getCompletedReceiptsMap(taskDto.getReceiptDtoToReviewMap(fileParser));
        receiptDtoToReviewMap.entrySet()
                .forEach(entry -> {
                    checkDateTimeFormat(entry);
                    checkMonetaryValuesFormat(entry);
                });

        log.info("The data format check filter has been successfully passed");
    }

    private void checkDateTimeFormat(Map.Entry<String, CompletedReceiptDto> receiptDtoEntry)
            throws IncorrectDateTimeFormat {
        CompletedReceiptDto.DateTimeInfo dateTimeInfo = receiptDtoEntry.getValue().getDateTimeInfo();

        try {
            LocalTime.parse(dateTimeInfo.getTime(), DateTimeFormatter.ofPattern(TIME_PATTERN));
            LocalDate.parse(dateTimeInfo.getDate(), DateTimeFormatter.ofPattern(DATE_PATTERN));

        } catch (DateTimeParseException e) {
            throw new IncorrectDateTimeFormat(substringToDot(receiptDtoEntry.getKey()));
        }
    }

    private void checkMonetaryValuesFormat(Map.Entry<String, CompletedReceiptDto> receiptDtoEntry)
            throws IncorrectDateTimeFormat {
        List<String> goodDescriptions = new ArrayList<>();

        receiptDtoEntry.getValue().getGoodsInfoList()
                .stream()
                .filter(not(getGoodsInfoPredicate()))
                .forEach(goodsInfo -> goodDescriptions.add(goodsInfo.getDescription()));

        Optional.of(receiptDtoEntry.getValue().getTotalInfo())
                .stream()
                .filter(not(getTotalInfoPredicate()))
                .findAny()
                .ifPresent(totalInfo -> goodDescriptions.add(Constant.TOTAL_HEADER));

        if (!goodDescriptions.isEmpty()) {
            throw new IncorrectMonetaryValueFormat(substringToDot(receiptDtoEntry.getKey()),
                    goodDescriptions.toString());
        }
    }

    private static Predicate<CompletedReceiptDto.GoodsInfo> getGoodsInfoPredicate() {
        return goodsInfo -> Pattern.matches(MONETARY_VALUE_PATTERN, goodsInfo.getPrice()) &&
                Pattern.matches(MONETARY_VALUE_PATTERN, goodsInfo.getDiscount()) &&
                Pattern.matches(MONETARY_VALUE_PATTERN, goodsInfo.getTotal());
    }

    private static Predicate<CompletedReceiptDto.TotalInfo> getTotalInfoPredicate() {
        return totalInfo -> Pattern.matches(MONETARY_VALUE_PATTERN, totalInfo.getTotalPrice()) &&
                Pattern.matches(MONETARY_VALUE_PATTERN, totalInfo.getTotalDiscount()) &&
                Pattern.matches(MONETARY_VALUE_PATTERN, totalInfo.getTotalWithDiscount());
    }

    @Override
    public int getOrder() {
        return 40;
    }

}
