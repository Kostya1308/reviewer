package ru.clevertec.courses.reviewer.checker.console;

import static java.util.function.Predicate.not;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.clevertec.courses.reviewer.dto.CompletedReceiptDto;
import ru.clevertec.courses.reviewer.dto.TaskDto;
import ru.clevertec.courses.reviewer.exception.DuplicateGoodsException;
import ru.clevertec.courses.reviewer.exception.IncorrectGoodListException;
import ru.clevertec.courses.reviewer.parser.FileParser;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class ProductNomenclatureChecker extends AbstractConsoleChecker {

    private final FileParser fileParser;

    @Override
    public void check(TaskDto taskDto) {
        log.info("Running the filter to check for goods nomenclature");

        var correctReceiptDtoMap = getCompletedReceiptsMap(taskDto.getCorrectReceiptDtoMap(fileParser));
        var receiptDtoToReviewMap = getCompletedReceiptsMap(taskDto.getReceiptDtoToReviewMap(fileParser));

        receiptDtoToReviewMap.forEach((key, value) -> {
            checkForDuplicates(key, getDescriptions(value));
            checkForExistingGoods(key, getDescriptions(correctReceiptDtoMap.get(key)), getDescriptions(value));
        });

        log.info("The filter to check for goods nomenclature has been successfully passed");
    }

    private void checkForDuplicates(String fileName, List<String> descriptions) {
        log.info("Starts checking the existence of duplicates on the receipt, obtained by running the application" +
                " using the parameters '{}'", fileName);

        HashSet<String> uniqueDescriptions = new HashSet<>();
        List<String> duplicates = descriptions.stream()
                .filter(description -> !uniqueDescriptions.add(description))
                .toList();

        if (!duplicates.isEmpty()) {
            throw new DuplicateGoodsException(fileName, duplicates);
        }

        log.info("Checking the existence of duplicates on the receipt, obtained by running the application" +
                " using the parameters '{}' has been successfully passed", fileName);
    }

    private void checkForExistingGoods(String fileName,
                                       List<String> correctDescriptions,
                                       List<String> reviewedDescriptions) throws IncorrectGoodListException {
        log.info("Starts checking the existence of goods on the receipt, obtained by running the application" +
                " using the parameters '{}'", fileName);

        List<String> requiredDescriptions = new ArrayList<>();
        List<String> redundantDescriptions = new ArrayList<>();

        correctDescriptions.stream()
                .filter(not(reviewedDescriptions::contains))
                .forEach(requiredDescriptions::add);

        reviewedDescriptions.stream()
                .filter(not(correctDescriptions::contains))
                .forEach(redundantDescriptions::add);

        if (!redundantDescriptions.isEmpty() || !requiredDescriptions.isEmpty()) {
            throw new IncorrectGoodListException(fileName, requiredDescriptions, redundantDescriptions);
        }

        log.info("Checking the the existence of goods on the receipt, obtained by running the application" +
                " using the parameters '{}' has been successfully passed", fileName);

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
