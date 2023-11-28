package ru.clevertec.courses.reviewer.processor;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.clevertec.courses.reviewer.dto.CompletedReceiptDto;
import ru.clevertec.courses.reviewer.dto.TaskDto;
import ru.clevertec.courses.reviewer.exception.IncorrectGoodListException;
import ru.clevertec.courses.reviewer.parser.FileParser;

import static java.util.function.Predicate.not;
import static ru.clevertec.courses.reviewer.util.FileUtil.*;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class ExistenceGoodsProcessor extends AbstractCheckingProcessor {

    private final FileParser fileParser;

    @Override
    public void check(TaskDto taskDto) {
        log.info("Running the filter to check for required/redundant goods");

        var correctReceiptDtoMap = getCompletedReceiptsMap(taskDto.getCorrectReceiptDtoMap(fileParser));
        var receiptDtoToReviewMap = getCompletedReceiptsMap(taskDto.getReceiptDtoToReviewMap(fileParser));

        correctReceiptDtoMap.forEach((key, value) ->
                checkForExistingGoods(key, getDescriptions(value), getDescriptions(receiptDtoToReviewMap.get(key)))
        );

        log.info("The filter to check for required/redundant goods has been successfully passed");
    }

    private void checkForExistingGoods(String fileName,
                                       List<String> correctDescriptions,
                                       List<String> reviewedDescriptions) throws IncorrectGoodListException {
        List<String> requiredDescriptions = new ArrayList<>();
        List<String> redundantDescriptions = new ArrayList<>();

        correctDescriptions.stream()
                .filter(not(reviewedDescriptions::contains))
                .forEach(requiredDescriptions::add);

        reviewedDescriptions.stream()
                .filter(not(correctDescriptions::contains))
                .forEach(redundantDescriptions::add);

        if (!redundantDescriptions.isEmpty() || !requiredDescriptions.isEmpty()) {
            throw new IncorrectGoodListException(substringToDot(fileName), requiredDescriptions, redundantDescriptions);
        }
    }

    private static List<String> getDescriptions(CompletedReceiptDto completedReceiptDto) {
        return completedReceiptDto.getGoodsInfoList().stream()
                .map(CompletedReceiptDto.GoodsInfo::getDescription)
                .toList();
    }

    @Override
    public int getOrder() {
        return 30;
    }

}
