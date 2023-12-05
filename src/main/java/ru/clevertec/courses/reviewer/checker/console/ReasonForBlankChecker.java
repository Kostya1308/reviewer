package ru.clevertec.courses.reviewer.checker.console;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.clevertec.courses.reviewer.dto.BlankReceiptDto;
import ru.clevertec.courses.reviewer.dto.TaskDto;
import ru.clevertec.courses.reviewer.exception.IncorrectErrorMessageException;
import ru.clevertec.courses.reviewer.parser.FileParser;

import static java.util.function.Predicate.not;
import static ru.clevertec.courses.reviewer.util.FileUtil.*;

import java.util.Map;
import java.util.Objects;
import java.util.function.Predicate;

@Slf4j
@Component
@RequiredArgsConstructor
public class ReasonForBlankChecker extends AbstractConsoleChecker {

    private final FileParser fileParser;

    @Override
    public void check(TaskDto taskDto) {
        log.info("Running the blank cheque cause check filter");

        var correctReceiptDtoMap = getBlankReceiptsMap(taskDto.getCorrectReceiptDtoMap(fileParser));
        var receiptDtoToReviewMap = getBlankReceiptsMap(taskDto.getReceiptDtoToReviewMap(fileParser));

        correctReceiptDtoMap.entrySet().stream()
                .filter(not(getNonEqualsMessagePredicate(receiptDtoToReviewMap)))
                .findAny()
                .ifPresent(entry -> {
                    throw new IncorrectErrorMessageException(substringToDot(entry.getKey()));
                });

        log.info("The blank cheque cause check filter has been successfully passed");
    }

    private static Predicate<Map.Entry<String, BlankReceiptDto>> getNonEqualsMessagePredicate(Map<String, BlankReceiptDto> receiptDtoToReviewMap) {
        return entry -> {
            BlankReceiptDto blankReceiptDto = receiptDtoToReviewMap.get(entry.getKey());
            return Objects.equals(blankReceiptDto.getErrorMessage(), entry.getValue().getErrorMessage());
        };
    }

    @Override
    public int getOrder() {
        return 20;
    }
}
