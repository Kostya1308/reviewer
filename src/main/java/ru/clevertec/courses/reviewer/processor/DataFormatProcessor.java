package ru.clevertec.courses.reviewer.processor;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.clevertec.courses.reviewer.constant.Constant;
import ru.clevertec.courses.reviewer.dto.CompletedReceiptDto;
import ru.clevertec.courses.reviewer.dto.TaskDto;
import ru.clevertec.courses.reviewer.entity.LaunchLine;
import ru.clevertec.courses.reviewer.exception.IncorrectDateTimeFormat;
import ru.clevertec.courses.reviewer.exception.IncorrectMonetaryValueFormat;
import ru.clevertec.courses.reviewer.repository.LaunchLineRepository;

import static java.util.function.Predicate.not;

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
public class DataFormatProcessor extends AbstractCheckingProcessor {


    private static final String TIME_PATTERN = "HH:mm:ss";
    private static final String DATE_PATTERN = "dd.MM.yyyy";
    private static final String MONETARY_VALUE_PATTERN = "\\d+\\.\\d{2}\\$";

    private final LaunchLineRepository launchLineRepository;

    @Override
    public void check(TaskDto taskDto) {
        Map<Integer, CompletedReceiptDto> receiptDtoToReviewMap = getCompletedReceipts(taskDto.getReceiptDtoToReviewMap());

        receiptDtoToReviewMap.entrySet()
                .forEach(entry -> {
                    checkDateTimeFormat(entry);
                    checkMonetaryValuesFormat(entry);
                });
    }

    private void checkDateTimeFormat(Map.Entry<Integer, CompletedReceiptDto> receiptDtoEntry) {
        CompletedReceiptDto.DateTimeInfo dateTimeInfo = receiptDtoEntry.getValue().getDateTimeInfo();

        try {
            LocalTime.parse(dateTimeInfo.getTime(), DateTimeFormatter.ofPattern(TIME_PATTERN));
            LocalDate.parse(dateTimeInfo.getDate(), DateTimeFormatter.ofPattern(DATE_PATTERN));

        } catch (DateTimeParseException e) {
            String line = launchLineRepository.findById(receiptDtoEntry.getKey())
                    .map(LaunchLine::getLine)
                    .orElse(null);
            throw new IncorrectDateTimeFormat(line);
        }
    }

    private void checkMonetaryValuesFormat(Map.Entry<Integer, CompletedReceiptDto> receiptDtoEntry) {
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
            String line = launchLineRepository.findById(receiptDtoEntry.getKey())
                    .map(LaunchLine::getLine)
                    .orElse(null);
            throw new IncorrectMonetaryValueFormat(line, goodDescriptions.toString());
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
        return 30;
    }

}
