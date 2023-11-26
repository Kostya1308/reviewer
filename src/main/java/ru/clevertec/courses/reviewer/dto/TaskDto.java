package ru.clevertec.courses.reviewer.dto;

import lombok.Builder;
import lombok.Data;

import java.util.Map;

@Data
@Builder
public class TaskDto {

    private Map<Integer, ReceiptDto> correctReceiptDtoMap;
    private Map<Integer, ReceiptDto> receiptDtoToReviewMap;

}
