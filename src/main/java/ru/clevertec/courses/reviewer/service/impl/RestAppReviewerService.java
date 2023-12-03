package ru.clevertec.courses.reviewer.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.clevertec.courses.reviewer.checker.rest.AbstractRestChecker;
import ru.clevertec.courses.reviewer.service.ReviewerService;

import static ru.clevertec.courses.reviewer.constant.Constant.REST_BRANCH;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RestAppReviewerService implements ReviewerService {

    private final List<AbstractRestChecker> restCheckers;

    @Override
    public void reviewTasks() {
        List<String> exceptionMessages = getExceptionMessages(restCheckers);

        exceptionMessages.stream()
                .findAny()
                .ifPresent(message -> {
                    throw new RuntimeException(exceptionMessages.toString());
                });

    }

    private List<String> getExceptionMessages(List<AbstractRestChecker> restCheckers) {
        List<String> exceptionMessages = new ArrayList<>();
        try {
            restCheckers.forEach(AbstractRestChecker::check);
        } catch (RuntimeException e) {
            exceptionMessages.add(e.getMessage());
        }

        return exceptionMessages;
    }

    @Override
    public List<String> getBranchNames() {
        return List.of(REST_BRANCH);
    }
}
