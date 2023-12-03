package ru.clevertec.courses.reviewer.processor;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import ru.clevertec.courses.reviewer.constant.Constant;
import ru.clevertec.courses.reviewer.dto.CompletedReceiptDto;
import ru.clevertec.courses.reviewer.dto.TaskDto;
import ru.clevertec.courses.reviewer.exception.CalculationException;
import ru.clevertec.courses.reviewer.parser.FileParser;

import static java.util.function.Predicate.not;
import static ru.clevertec.courses.reviewer.util.FileUtil.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Predicate;

@Slf4j
@Component
@RequiredArgsConstructor
public class CalculationProcessor extends AbstractCheckingProcessor {

    private final FileParser fileParser;

    @Override
    public void check(TaskDto taskDto) {
        log.info("Running the calculation check filter");

        var receiptDtoToReviewMap = getCompletedReceiptsMap(taskDto.getReceiptDtoToReviewMap(fileParser));
        var correctReceiptDtoMap = getCompletedReceiptsMap(taskDto.getCorrectReceiptDtoMap(fileParser));

        receiptDtoToReviewMap.forEach((key, value) -> checkCalculating(key, value, correctReceiptDtoMap.get(key)));

        log.info("The calculation check filter has been successfully passed");
    }

    private void checkCalculating(String fileName,
                                  CompletedReceiptDto reviewedCompletedReceiptDto,
                                  CompletedReceiptDto correctCompletedReceiptDto) throws CalculationException {
        List<String> descriptionList = new ArrayList<>();

        List<CompletedReceiptDto.GoodsInfo> reviewedGoodsInfoList = reviewedCompletedReceiptDto.getGoodsInfoList();
        List<CompletedReceiptDto.GoodsInfo> correctGoodsInfoList = correctCompletedReceiptDto.getGoodsInfoList();

        CompletedReceiptDto.TotalInfo reviewedTotalInfo = reviewedCompletedReceiptDto.getTotalInfo();
        CompletedReceiptDto.TotalInfo correctTotalInfo = correctCompletedReceiptDto.getTotalInfo();

        reviewedGoodsInfoList.forEach(reviewedGoodsInfo -> correctGoodsInfoList.stream()
                .filter(goodsInfo -> Objects.equals(goodsInfo.getDescription(), reviewedGoodsInfo.getDescription()))
                .filter(not(getCorrectGoodsPredicate(reviewedGoodsInfo)))
                .forEach(goodsInfo -> descriptionList.add(goodsInfo.getDescription())));

        Optional.ofNullable(reviewedTotalInfo)
                .filter(not(getCorrectTotalPredicate(correctTotalInfo)))
                .ifPresent(totalInfo -> descriptionList.add(Constant.TOTAL_HEADER));

        if (!descriptionList.isEmpty()) {
            throw new CalculationException(substringToDot(fileName), descriptionList.toString());
        }
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
        return Ordered.LOWEST_PRECEDENCE;
    }

}
