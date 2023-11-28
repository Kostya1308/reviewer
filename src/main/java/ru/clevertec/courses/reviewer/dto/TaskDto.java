package ru.clevertec.courses.reviewer.dto;

import lombok.Builder;
import lombok.Data;
import ru.clevertec.courses.reviewer.parser.FileParser;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Data
@Builder
public class TaskDto {

    private List<File> correctFiles;
    private List<File> filesToReview;
    private Map<String, ReceiptDto> correctReceiptDtoMap;
    private Map<String, ReceiptDto> receiptDtoToReviewMap;

    public Map<String, ReceiptDto> getCorrectReceiptDtoMap(FileParser fileParser) {
        return Optional.ofNullable(correctReceiptDtoMap)
                .orElseGet(() -> {
                    this.setCorrectReceiptDtoMap(correctFiles.stream()
                            .collect(Collectors.toMap(File::getName, fileParser::parseCsvFile)));
                    return correctReceiptDtoMap;
                });
    }

    public Map<String, ReceiptDto> getReceiptDtoToReviewMap(FileParser fileParser) {
        return Optional.ofNullable(receiptDtoToReviewMap)
                .orElseGet(() -> {
                    receiptDtoToReviewMap = filesToReview.stream()
                            .collect(Collectors.toMap(File::getName, fileParser::parseCsvFile));
                    return receiptDtoToReviewMap;
                });
    }
}
