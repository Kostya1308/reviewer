package ru.clevertec.courses.reviewer.processor;

import lombok.RequiredArgsConstructor;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import ru.clevertec.courses.reviewer.constant.Constant;
import ru.clevertec.courses.reviewer.dto.CompletedReceiptDto;
import ru.clevertec.courses.reviewer.dto.TaskDto;
import ru.clevertec.courses.reviewer.entity.LaunchLine;
import ru.clevertec.courses.reviewer.exception.CalculationException;
import ru.clevertec.courses.reviewer.service.LaunchLineService;

import static java.util.function.Predicate.not;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Predicate;

@Component
@RequiredArgsConstructor
public class CalculationProcessor extends AbstractCheckingProcessor {

    private final LaunchLineService launchLineService;

    @Override
    public void check(TaskDto taskDto) {
        Map<Integer, CompletedReceiptDto> receiptDtoToReviewMap = getCompletedReceipts(taskDto.getReceiptDtoToReviewMap());
        Map<Integer, CompletedReceiptDto> correctReceiptDtoMap = getCompletedReceipts(taskDto.getCorrectReceiptDtoMap());

        receiptDtoToReviewMap.forEach((key, value) -> checkCalculating(key, value, correctReceiptDtoMap.get(key)));
    }

    private void checkCalculating(Integer lineId, CompletedReceiptDto reviewedCompletedReceiptDto, CompletedReceiptDto correctCompletedReceiptDto) {
        List<String> descriptionList = new ArrayList<>();

        List<CompletedReceiptDto.GoodsInfo> reviewedGoodsInfoList = reviewedCompletedReceiptDto.getGoodsInfoList();
        List<CompletedReceiptDto.GoodsInfo> correctGoodsInfoList = correctCompletedReceiptDto.getGoodsInfoList();

        CompletedReceiptDto.TotalInfo reviewedTotalInfo = reviewedCompletedReceiptDto.getTotalInfo();
        CompletedReceiptDto.TotalInfo correctTotalInfo = correctCompletedReceiptDto.getTotalInfo();

        reviewedGoodsInfoList.forEach(reviewedGoodsInfo -> correctGoodsInfoList.stream()
                .filter(goodsInfo -> Objects.equals(goodsInfo.getDescription(), reviewedGoodsInfo.getDescription()))
                .filter(not(getCorrectGoodsInfoPredicate(reviewedGoodsInfo)))
                .forEach(goodsInfo -> descriptionList.add(goodsInfo.getDescription())));

        Optional.ofNullable(reviewedTotalInfo)
                .filter(not(getCorrectTotalInfoPredicate(correctTotalInfo)))
                .ifPresent(totalInfo -> descriptionList.add(Constant.TOTAL_HEADER));

        if (!descriptionList.isEmpty()) {
            String line = launchLineService.getArgsByLaunchLineId(lineId);
            throw new CalculationException(line, descriptionList.toString());
        }
    }

    private static Predicate<CompletedReceiptDto.GoodsInfo> getCorrectGoodsInfoPredicate(CompletedReceiptDto.GoodsInfo reviewedGoodsInfo) {
        return correctGoodsInfo ->
                Objects.equals(correctGoodsInfo.getPrice(), reviewedGoodsInfo.getPrice()) &&
                        Objects.equals(correctGoodsInfo.getTotal(), reviewedGoodsInfo.getTotal()) &&
                        Objects.equals(correctGoodsInfo.getDiscount(), reviewedGoodsInfo.getDiscount()) &&
                        Objects.equals(correctGoodsInfo.getQuantity(), reviewedGoodsInfo.getQuantity()) &&
                        Objects.equals(correctGoodsInfo.getDescription(), reviewedGoodsInfo.getDescription());
    }

    private static Predicate<CompletedReceiptDto.TotalInfo> getCorrectTotalInfoPredicate(CompletedReceiptDto.TotalInfo correctTotalInfo) {
        return reviewedTotal -> Objects.equals(reviewedTotal.getTotalPrice(), correctTotalInfo.getTotalPrice()) &&
                Objects.equals(reviewedTotal.getTotalDiscount(), correctTotalInfo.getTotalDiscount()) &&
                Objects.equals(reviewedTotal.getTotalWithDiscount(), correctTotalInfo.getTotalWithDiscount());
    }

    @Override
    public int getOrder() {
        return Ordered.LOWEST_PRECEDENCE;
    }

}
