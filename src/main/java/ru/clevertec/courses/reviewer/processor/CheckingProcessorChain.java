package ru.clevertec.courses.reviewer.processor;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.AnnotationAwareOrderComparator;
import org.springframework.stereotype.Component;
import ru.clevertec.courses.reviewer.dto.TaskDto;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class CheckingProcessorChain {

    private final List<AbstractCheckingProcessor> abstractCheckingProcessors;

    @PostConstruct
    public void sortCheckingProcessors() {
        abstractCheckingProcessors.sort(AnnotationAwareOrderComparator.INSTANCE);
    }

    public void runChain(TaskDto taskDto) {
        log.info("Starting a chain of checking filters");
        abstractCheckingProcessors.forEach(checkingProcessor -> checkingProcessor.check(taskDto));
        log.info("All checking filters have been successfully passed");
    }

}
