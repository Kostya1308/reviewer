package ru.clevertec.courses.reviewer.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.clevertec.courses.reviewer.downloader.FileDownloader;
import ru.clevertec.courses.reviewer.dto.ReceiptDto;
import ru.clevertec.courses.reviewer.dto.TaskDto;
import ru.clevertec.courses.reviewer.entity.LaunchLine;
import ru.clevertec.courses.reviewer.exception.RedundantReceiptIsPresentException;
import ru.clevertec.courses.reviewer.exception.RequiredReceiptIsMissingException;
import ru.clevertec.courses.reviewer.parser.FileParser;
import ru.clevertec.courses.reviewer.processor.CheckingProcessorChain;
import ru.clevertec.courses.reviewer.repository.LaunchLineRepository;
import ru.clevertec.courses.reviewer.service.ReviewerService;


import static ru.clevertec.courses.reviewer.util.FileUtil.*;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReviewerServiceImpl implements ReviewerService {

    @Value("${app.tasks-path}")
    private String tasksPath;

    @Value("${app.branch}")
    private Integer branchId;

    private final FileParser fileParser;
    private final FileDownloader fileDownloader;
    private final ResourceLoader resourceLoader;
    private final LaunchLineRepository launchLineRepository;
    private final CheckingProcessorChain checkingProcessorChain;

    @Override
    @SneakyThrows
    public void reviewTasks() {
        List<LaunchLine> launchLines = launchLineRepository.findByBranch_Id(branchId);
        File[] filesToReview = resourceLoader.getResource(tasksPath).getFile().listFiles();
        checkForExistence(filesToReview, launchLines);

        Map<Integer, ReceiptDto> correctReceiptDtoMap = launchLines.stream()
                .collect(Collectors.toMap(LaunchLine::getId, launchLine ->
                        fileParser.parseCsvFile(fileDownloader.downloadFromPath(launchLine.getResultPath())))
                );

        Map<Integer, ReceiptDto> receiptDtoToReviewMap = Arrays.stream(Objects.requireNonNull(filesToReview))
                .collect(Collectors.toMap(file ->
                        Integer.valueOf(substringToDot(file.getName())), fileParser::parseCsvFile)
                );

        TaskDto taskDto = TaskDto.builder()
                .correctReceiptDtoMap(correctReceiptDtoMap)
                .receiptDtoToReviewMap(receiptDtoToReviewMap)
                .build();

        checkingProcessorChain.runChain(taskDto);
    }

    private void checkForExistence(File[] filesToReview, List<LaunchLine> launchLines) {
        List<Integer> list = Arrays.stream(Objects.requireNonNull(filesToReview))
                .map(file -> substringToDot(file.getName()))
                .map(Integer::valueOf)
                .toList();

        launchLines.stream()
                .map(LaunchLine::getId)
                .filter(id -> !list.contains(id))
                .findAny()
                .flatMap(launchLineRepository::findById)
                .ifPresent(launchLine -> {
                    throw new RequiredReceiptIsMissingException(launchLine.getArguments());
                });

        list.stream()
                .filter(id -> !launchLines.stream().map(LaunchLine::getId).toList().contains(id))
                .findAny()
                .ifPresent(id -> {
                    throw new RedundantReceiptIsPresentException();
                });
    }

}
