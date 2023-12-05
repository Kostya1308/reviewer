package ru.clevertec.courses.reviewer.factory;

import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.clevertec.courses.reviewer.exception.ReviewerNotFoundException;
import ru.clevertec.courses.reviewer.service.ReviewerService;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class ReviewerFactory {

    private final List<ReviewerFactory.Node> nodes = new ArrayList<>();

    private final List<ReviewerService> reviewerServices;

    @PostConstruct
    public void fill() {
        reviewerServices.forEach(reviewer ->
                nodes.add(new ReviewerFactory.Node(reviewer.getBranchNames(), reviewer))
        );
    }

    public ReviewerService getReviewer(String branchName) throws ReviewerNotFoundException {
        return this.nodes.stream()
                .filter(node -> node.getBranchNames().contains(branchName))
                .findAny()
                .map(ReviewerFactory.Node::getReviewerService)
                .orElseThrow(() -> new ReviewerNotFoundException(branchName));
    }

    @Data
    @AllArgsConstructor
    private static class Node {
        private List<String> branchNames;
        private ReviewerService reviewerService;
    }

}
