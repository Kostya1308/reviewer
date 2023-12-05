package ru.clevertec.courses.reviewer.runner;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import ru.clevertec.courses.reviewer.factory.ReviewerFactory;
import ru.clevertec.courses.reviewer.service.ReviewerService;

@Slf4j
@Component
@RequiredArgsConstructor
public class ApplicationStartupRunner {

    @Value("${app.branch-name}")
    private String branchName;

    private final ReviewerFactory reviewerFactory;

    @EventListener
    public void run(ContextRefreshedEvent contextRefreshedEvent) {
        log.info(contextRefreshedEvent.toString());
        ReviewerService reviewerService = reviewerFactory.getReviewer(branchName);

        reviewerService.reviewTasks();
    }

}
