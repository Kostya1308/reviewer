package ru.clevertec.courses.reviewer.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.context.annotation.Configuration;
import ru.clevertec.courses.reviewer.exception.FailedConsoleAppReviewException;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

@Slf4j
@Aspect
@Configuration
public class ExceptionWriterAspect {

    private static final String FILE_PATH = "exception.txt";

    @AfterThrowing(pointcut = "getConsoleCheckerPointcut()", throwing = "ex")
    public void writeToFile(FailedConsoleAppReviewException ex) {
        try (var bufferedWriter = new BufferedWriter(new FileWriter(FILE_PATH))) {
            bufferedWriter.write(ex.getMessage());

        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    @Pointcut(value = "execution(* ru.clevertec.courses.reviewer.checker.console.*.*(..))")
    public void getConsoleCheckerPointcut() {
    }

}
