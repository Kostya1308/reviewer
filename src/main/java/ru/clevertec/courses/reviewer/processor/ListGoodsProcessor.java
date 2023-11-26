package ru.clevertec.courses.reviewer.processor;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.clevertec.courses.reviewer.dto.CompletedReceiptDto;
import ru.clevertec.courses.reviewer.dto.TaskDto;
import ru.clevertec.courses.reviewer.entity.LaunchLine;
import ru.clevertec.courses.reviewer.exception.IncorrectGoodListException;
import ru.clevertec.courses.reviewer.repository.LaunchLineRepository;

import static java.util.function.Predicate.not;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class ListGoodsProcessor extends AbstractCheckingProcessor {

    private final LaunchLineRepository launchLineRepository;

    @Override
    public void check(TaskDto taskDto) {
        Map<Integer, CompletedReceiptDto> correctReceiptDtoMap = getCompletedReceipts(taskDto.getCorrectReceiptDtoMap());
        Map<Integer, CompletedReceiptDto> receiptDtoToReviewMap = getCompletedReceipts(taskDto.getReceiptDtoToReviewMap());

        correctReceiptDtoMap.forEach((key, value) ->
                checkForExistingGoods(key, getDescriptions(value), getDescriptions(receiptDtoToReviewMap.get(key)))
        );
    }

    private void checkForExistingGoods(Integer lineId, List<String> correctDescriptions, List<String> reviewedDescriptions) {
        List<String> requiredDescriptions = new ArrayList<>();
        List<String> redundantDescriptions = new ArrayList<>();

        correctDescriptions.stream()
                .filter(not(reviewedDescriptions::contains))
                .forEach(requiredDescriptions::add);

        reviewedDescriptions.stream()
                .filter(not(correctDescriptions::contains))
                .forEach(redundantDescriptions::add);

        if (!redundantDescriptions.isEmpty() || !requiredDescriptions.isEmpty()) {
            String line = launchLineRepository.findById(lineId)
                    .map(LaunchLine::getLine)
                    .orElse(null);
            throw new IncorrectGoodListException(line, requiredDescriptions, redundantDescriptions);
        }
    }


    private static List<String> getDescriptions(CompletedReceiptDto completedReceiptDto) {
        return completedReceiptDto.getGoodsInfoList().stream()
                .map(CompletedReceiptDto.GoodsInfo::getDescription)
                .toList();
    }

    @Override
    public int getOrder() {
        return 20;
    }

}
