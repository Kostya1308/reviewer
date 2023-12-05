package ru.clevertec.courses.reviewer.checker.console;

import org.springframework.core.Ordered;
import ru.clevertec.courses.reviewer.dto.BlankReceiptDto;
import ru.clevertec.courses.reviewer.dto.CompletedReceiptDto;
import ru.clevertec.courses.reviewer.dto.ReceiptDto;
import ru.clevertec.courses.reviewer.dto.TaskDto;

import java.util.Map;
import java.util.stream.Collectors;

public abstract class AbstractConsoleChecker implements Ordered {

    public abstract void check(TaskDto taskDto);

    protected Map<String, CompletedReceiptDto> getCompletedReceiptsMap(Map<String, ReceiptDto> receiptDtoMap) {
        return receiptDtoMap.entrySet()
                .stream()
                .filter(entry -> entry.getValue() instanceof CompletedReceiptDto)
                .collect(Collectors.toMap(Map.Entry::getKey, entry -> (CompletedReceiptDto) entry.getValue()));
    }

    protected Map<String, BlankReceiptDto> getBlankReceiptsMap(Map<String, ReceiptDto> receiptDtoMap) {
        return receiptDtoMap.entrySet()
                .stream()
                .filter(entry -> entry.getValue() instanceof BlankReceiptDto)
                .collect(Collectors.toMap(Map.Entry::getKey, entry -> (BlankReceiptDto) entry.getValue()));
    }

}
