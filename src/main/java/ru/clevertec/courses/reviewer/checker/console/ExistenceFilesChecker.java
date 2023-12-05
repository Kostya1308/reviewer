package ru.clevertec.courses.reviewer.checker.console;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.clevertec.courses.reviewer.dto.TaskDto;
import ru.clevertec.courses.reviewer.exception.RedundantReceiptIsPresentException;
import ru.clevertec.courses.reviewer.exception.RequiredReceiptIsMissingException;

import static java.util.function.Predicate.not;
import static ru.clevertec.courses.reviewer.util.FileUtil.*;

import java.io.File;
import java.util.List;
import java.util.function.Predicate;

@Slf4j
@Component
@RequiredArgsConstructor
public class ExistenceFilesChecker extends AbstractConsoleChecker {

    @Override
    public void check(TaskDto taskDto) {
        log.info("Running the filter to check for required/redundant files");

        taskDto.getCorrectFiles().stream()
                .map(File::getName)
                .filter(not(getContainsPredicate(taskDto.getFilesToReview())))
                .findAny()
                .ifPresent(name -> {
                    throw new RequiredReceiptIsMissingException(substringToDot(name));
                });

        taskDto.getFilesToReview().stream()
                .map(File::getName)
                .filter(not(getContainsPredicate(taskDto.getCorrectFiles())))
                .findAny()
                .ifPresent(name -> {
                    throw new RedundantReceiptIsPresentException();
                });

        log.info("The filter to check for required/redundant files has been successfully passed");
    }

    @Override
    public int getOrder() {
        return HIGHEST_PRECEDENCE;
    }

    private static Predicate<String> getContainsPredicate(List<File> files) {
        return name -> files.stream()
                .map(File::getName)
                .toList()
                .contains(name);
    }
}
