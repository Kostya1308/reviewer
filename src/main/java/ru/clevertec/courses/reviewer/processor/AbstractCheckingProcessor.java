package ru.clevertec.courses.reviewer.processor;

import org.springframework.core.Ordered;
import ru.clevertec.courses.reviewer.dto.BlankReceiptDto;
import ru.clevertec.courses.reviewer.dto.CompletedReceiptDto;
import ru.clevertec.courses.reviewer.dto.ReceiptDto;
import ru.clevertec.courses.reviewer.dto.TaskDto;

import java.util.Map;
import java.util.stream.Collectors;

public abstract class AbstractCheckingProcessor implements Ordered {

    public abstract void check(TaskDto taskDto);

    Map<Integer, CompletedReceiptDto> getCompletedReceipts(Map<Integer, ReceiptDto> receiptDtoMap) {
        return receiptDtoMap.entrySet()
                .stream()
                .filter(entry -> entry.getValue() instanceof CompletedReceiptDto)
                .collect(Collectors.toMap(Map.Entry::getKey, entry -> (CompletedReceiptDto) entry.getValue()));
    }

    Map<Integer, BlankReceiptDto> getBlankReceipts(Map<Integer, ReceiptDto> receiptDtoMap) {
        return receiptDtoMap.entrySet()
                .stream()
                .filter(entry -> entry.getValue() instanceof BlankReceiptDto)
                .collect(Collectors.toMap(Map.Entry::getKey, entry -> (BlankReceiptDto) entry.getValue()));
    }

}
