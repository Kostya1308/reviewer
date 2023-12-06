package ru.clevertec.courses.reviewer.checker.console;

import static java.util.function.Predicate.not;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.clevertec.courses.reviewer.constant.Constant;
import ru.clevertec.courses.reviewer.dto.CompletedReceiptDto;
import ru.clevertec.courses.reviewer.dto.TaskDto;
import ru.clevertec.courses.reviewer.exception.CalculationException;
import ru.clevertec.courses.reviewer.parser.FileParser;

import java.util.ArrayList;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Predicate;

@Slf4j
@Component
@RequiredArgsConstructor
public class CalculationChecker extends AbstractConsoleChecker {

    private final FileParser fileParser;

    @Override
    public void check(TaskDto taskDto) {
        log.info("Running the calculation check filter");

        var receiptDtoToReviewMap = getCompletedReceiptsMap(taskDto.getReceiptDtoToReviewMap(fileParser));
        var correctReceiptDtoMap = getCompletedReceiptsMap(taskDto.getCorrectReceiptDtoMap(fileParser));

        receiptDtoToReviewMap.forEach((key, value) ->
                checkCalculating(key, value, correctReceiptDtoMap.get(key)));

        log.info("The calculation check filter has been successfully passed");
    }

    private void checkCalculating(String fileName,
                                  CompletedReceiptDto reviewedCompletedReceiptDto,
                                  CompletedReceiptDto correctCompletedReceiptDto) throws CalculationException {
        log.info("Starts checking the calculations on the receipt obtained by running the application " +
                "using the parameters '{}'", fileName);

        var descriptionList = new ArrayList<>();

        var reviewedGoodsInfoList = reviewedCompletedReceiptDto.getGoodsInfoList();
        var correctGoodsInfoList = correctCompletedReceiptDto.getGoodsInfoList();

        var reviewedTotalInfo = reviewedCompletedReceiptDto.getTotalInfo();
        var correctTotalInfo = correctCompletedReceiptDto.getTotalInfo();

        reviewedGoodsInfoList.forEach(reviewedGoodsInfo -> correctGoodsInfoList.stream()
                .filter(goodsInfo -> Objects.equals(goodsInfo.getDescription(), reviewedGoodsInfo.getDescription()))
                .filter(not(getCorrectGoodsPredicate(reviewedGoodsInfo)))
                .forEach(goodsInfo -> descriptionList.add(goodsInfo.getDescription())));

        Optional.ofNullable(reviewedTotalInfo)
                .filter(not(getCorrectTotalPredicate(correctTotalInfo)))
                .ifPresent(totalInfo -> descriptionList.add(Constant.TOTAL_HEADER));

        if (!descriptionList.isEmpty()) {
            throw new CalculationException(fileName, descriptionList.toString());
        }

        log.info("Checking the calculations on the receipt, obtained by running the application" +
                " using the parameters '{}' has been successfully passed", fileName);
    }

    private static Predicate<CompletedReceiptDto.GoodsInfo> getCorrectGoodsPredicate(CompletedReceiptDto.GoodsInfo reviewedGoodsInfo) {
        return correctGoodsInfo ->
                Objects.equals(correctGoodsInfo.getPrice(), reviewedGoodsInfo.getPrice()) &&
                        Objects.equals(correctGoodsInfo.getTotal(), reviewedGoodsInfo.getTotal()) &&
                        Objects.equals(correctGoodsInfo.getDiscount(), reviewedGoodsInfo.getDiscount()) &&
                        Objects.equals(correctGoodsInfo.getQuantity(), reviewedGoodsInfo.getQuantity()) &&
                        Objects.equals(correctGoodsInfo.getDescription(), reviewedGoodsInfo.getDescription());
    }

    private static Predicate<CompletedReceiptDto.TotalInfo> getCorrectTotalPredicate(CompletedReceiptDto.TotalInfo correctTotalInfo) {
        return reviewedTotal -> Objects.equals(reviewedTotal.getTotalPrice(), correctTotalInfo.getTotalPrice()) &&
                Objects.equals(reviewedTotal.getTotalDiscount(), correctTotalInfo.getTotalDiscount()) &&
                Objects.equals(reviewedTotal.getTotalWithDiscount(), correctTotalInfo.getTotalWithDiscount());
    }

    @Override
    public int getOrder() {
        return LOWEST_PRECEDENCE;
    }

}
