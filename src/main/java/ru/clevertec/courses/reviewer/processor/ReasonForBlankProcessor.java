package ru.clevertec.courses.reviewer.processor;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.clevertec.courses.reviewer.dto.BlankReceiptDto;
import ru.clevertec.courses.reviewer.dto.TaskDto;
import ru.clevertec.courses.reviewer.exception.IncorrectErrorMessageException;
import ru.clevertec.courses.reviewer.repository.LaunchLineRepository;

import java.util.Map;
import java.util.Objects;
import java.util.function.Predicate;

@Component
@RequiredArgsConstructor
public class ReasonForBlankProcessor extends AbstractCheckingProcessor {

    private final LaunchLineRepository launchLineRepository;

    @Override
    public void check(TaskDto taskDto) {
        Map<Integer, BlankReceiptDto> correctReceiptDtoMap = getBlankReceipts(taskDto.getCorrectReceiptDtoMap());
        Map<Integer, BlankReceiptDto> receiptDtoToReviewMap = getBlankReceipts(taskDto.getReceiptDtoToReviewMap());

        correctReceiptDtoMap.entrySet().stream()
                .filter(getNonEqualsMessagePredicate(receiptDtoToReviewMap))
                .findAny()
                .flatMap(entry -> launchLineRepository.findById(entry.getKey()))
                .ifPresent(line -> {
                    throw new IncorrectErrorMessageException(line.getLine());
                });

    }

    private static Predicate<Map.Entry<Integer, BlankReceiptDto>> getNonEqualsMessagePredicate(Map<Integer, BlankReceiptDto> receiptDtoToReviewMap) {
        return entry -> {
            BlankReceiptDto blankReceiptDto = receiptDtoToReviewMap.get(entry.getKey());
            return !Objects.equals(blankReceiptDto.getErrorMessage(), entry.getValue().getErrorMessage());
        };
    }

    @Override
    public int getOrder() {
        return 10;
    }
}
