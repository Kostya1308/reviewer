package ru.clevertec.courses.reviewer.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;
import ru.clevertec.courses.reviewer.dto.TaskDto;
import ru.clevertec.courses.reviewer.exception.ResourceNotFoundException;
import ru.clevertec.courses.reviewer.processor.CheckingProcessorChain;
import ru.clevertec.courses.reviewer.service.ReviewerService;

import java.io.File;
import java.util.Arrays;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReviewerServiceImpl implements ReviewerService {

    @Value("${app.correct-tasks-path}")
    private String correctTasksPath;

    @Value("${app.received-tasks-path}")
    private String receivedTasksPath;

    private final ResourceLoader resourceLoader;
    private final CheckingProcessorChain checkingProcessorChain;

    @Override
    @SneakyThrows
    public void reviewTasks() {
        File[] correctFiles = Optional.ofNullable(resourceLoader.getResource(correctTasksPath).getFile().listFiles())
                .orElseThrow(() -> new ResourceNotFoundException(correctTasksPath));
        File[] filesToReview = Optional.ofNullable(resourceLoader.getResource(receivedTasksPath).getFile().listFiles())
                .orElseThrow(() -> new ResourceNotFoundException(receivedTasksPath));
        log.info("Resource files have been found");

        TaskDto taskDto = TaskDto.builder()
                .correctFiles(Arrays.asList(correctFiles))
                .filesToReview(Arrays.asList(filesToReview))
                .build();

        checkingProcessorChain.runChain(taskDto);
    }

}
