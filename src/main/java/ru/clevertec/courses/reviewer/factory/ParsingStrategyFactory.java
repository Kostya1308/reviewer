package ru.clevertec.courses.reviewer.factory;

import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.clevertec.courses.reviewer.exception.IncorrectFileStructureException;
import ru.clevertec.courses.reviewer.parser.impl.ParsingStrategy;
import ru.clevertec.courses.reviewer.service.LaunchLineService;

import static ru.clevertec.courses.reviewer.util.FileUtil.*;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class ParsingStrategyFactory {

    private final List<Node> nodes = new ArrayList<>();

    private final List<ParsingStrategy> parsingStrategies;

    @PostConstruct
    public void fill() {
        parsingStrategies.forEach(strategy ->
                nodes.add(new Node(strategy.getHeader(), strategy))
        );
    }

    public ParsingStrategy getStrategy(String header) throws IncorrectFileStructureException {
        return this.nodes.stream()
                .filter(node -> header.contains(node.getHeader()))
                .findAny()
                .map(Node::getParsingStrategy)
                .orElseThrow(IncorrectFileStructureException::new);
    }

    @Data
    @AllArgsConstructor
    private static class Node {
        private String header;
        private ParsingStrategy parsingStrategy;
    }

}
