package ru.clevertec.courses.reviewer.runner;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import ru.clevertec.courses.reviewer.service.ReviewerService;

@Slf4j
@Component
@RequiredArgsConstructor
public class ApplicationStartupRunner {

    private final ReviewerService reviewerService;

    @EventListener
    public void run(ContextRefreshedEvent contextRefreshedEvent) {
        log.info(contextRefreshedEvent.toString());
        reviewerService.reviewTasks();
    }

}
