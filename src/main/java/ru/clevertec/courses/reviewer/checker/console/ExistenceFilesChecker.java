package ru.clevertec.courses.reviewer.checker.console;

import static java.util.function.Predicate.not;
import static ru.clevertec.courses.reviewer.util.FileUtil.substringToDot;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.clevertec.courses.reviewer.dto.TaskDto;
import ru.clevertec.courses.reviewer.exception.RedundantReceiptIsPresentException;
import ru.clevertec.courses.reviewer.exception.RequiredReceiptIsMissingException;

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

        log.info("Starts checking the presence of required receipts");
        taskDto.getCorrectFiles().stream()
                .map(File::getName)
                .filter(not(getContainsPredicate(taskDto.getFilesToReview())))
                .findAny()
                .ifPresent(name -> {
                    throw new RequiredReceiptIsMissingException(substringToDot(name));
                });
        log.info("Checking the presence of required receipts has been successfully passed - " +
                "all necessary receipts are present");

        log.info("Starts checking for the absence of redundant receipts");
        taskDto.getFilesToReview().stream()
                .map(File::getName)
                .filter(not(getContainsPredicate(taskDto.getCorrectFiles())))
                .findAny()
                .ifPresent(name -> {
                    throw new RedundantReceiptIsPresentException();
                });
        log.info("Checking the absence of redundant receipts has been successfully passed - " +
                "there aren't redundant receipts");

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
