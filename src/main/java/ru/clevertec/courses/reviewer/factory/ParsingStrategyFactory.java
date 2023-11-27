package ru.clevertec.courses.reviewer.factory;

import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.clevertec.courses.reviewer.entity.LaunchLine;
import ru.clevertec.courses.reviewer.exception.IncorrectFileStructureException;
import ru.clevertec.courses.reviewer.parser.ParsingStrategy;
import ru.clevertec.courses.reviewer.repository.LaunchLineRepository;
import ru.clevertec.courses.reviewer.service.LaunchLineService;

import static ru.clevertec.courses.reviewer.util.FileUtil.*;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class ParsingStrategyFactory {

    private final List<Node> nodes = new ArrayList<>();

    private final LaunchLineService launchLineService;
    private final List<ParsingStrategy> parsingStrategies;

    @PostConstruct
    public void parsingStrategyFactory() {
        parsingStrategies.forEach(strategy ->
                nodes.add(new Node(strategy.getHeader(), strategy))
        );
    }

    public ParsingStrategy getStrategy(String header, String fileName) {
        return this.nodes.stream()
                .filter(node -> header.contains(node.getHeader()))
                .findAny()
                .map(Node::getParsingStrategy)
                .orElseThrow(() -> {
                    String line = launchLineService.getArgsByLaunchLineId(Integer.valueOf(substringToDot(fileName)));
                    return new IncorrectFileStructureException(line);
                });
    }

    @Data
    @AllArgsConstructor
    private static class Node {
        private String header;
        private ParsingStrategy parsingStrategy;
    }

}
