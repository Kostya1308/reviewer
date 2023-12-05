package ru.clevertec.courses.reviewer.chain.impl;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.AnnotationAwareOrderComparator;
import org.springframework.stereotype.Component;
import ru.clevertec.courses.reviewer.checker.console.AbstractConsoleChecker;
import ru.clevertec.courses.reviewer.dto.TaskDto;
import ru.clevertec.courses.reviewer.chain.CheckerChain;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class ConsoleCheckerChain implements CheckerChain<TaskDto> {

    private final List<AbstractConsoleChecker> consoleCheckers;

    @PostConstruct
    public void sortCheckingProcessors() {
        consoleCheckers.sort(AnnotationAwareOrderComparator.INSTANCE);
    }

    public void runChain(TaskDto taskDto) {
        log.info("Starting a chain of checking filters");
        consoleCheckers.forEach(consoleChecker -> consoleChecker.check(taskDto));
        log.info("All checking filters have been successfully passed");
    }

}
